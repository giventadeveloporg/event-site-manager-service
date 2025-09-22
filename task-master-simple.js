#!/usr/bin/env node

import fs from 'fs';
import path from 'path';

// Simple task manager for WhatsApp integration project
class TaskMaster {
  constructor() {
    this.tasksPath = '.task-master/tasks/tasks.json';
  }

  loadTasks() {
    try {
      const data = JSON.parse(fs.readFileSync(this.tasksPath, 'utf8'));
      return data.tasks || [];
    } catch (error) {
      console.error('Error loading tasks:', error.message);
      return [];
    }
  }

  listTasks(statusFilter = null) {
    const tasks = this.loadTasks();

    console.log('\n📋 WhatsApp Integration Tasks');
    console.log('=' .repeat(60));

    if (tasks.length === 0) {
      console.log('No tasks found.');
      return;
    }

    const filteredTasks = statusFilter
      ? tasks.filter(t => t.status === statusFilter)
      : tasks;

    filteredTasks.forEach(task => {
      const statusIcon = this.getStatusIcon(task.status);
      const priorityColor = this.getPriorityColor(task.priority);

      console.log(`${statusIcon} [${task.id}] ${task.title}`);
      console.log(`    Status: ${task.status} | Priority: ${task.priority}`);
      console.log(`    Dependencies: ${task.dependencies?.join(', ') || 'None'}`);
      console.log(`    Description: ${task.description?.substring(0, 100)}...`);
      console.log('');
    });

    console.log(`\nTotal: ${filteredTasks.length} tasks`);
  }

  getStatusIcon(status) {
    switch (status) {
      case 'pending': return '⏳';
      case 'done': return '✅';
      case 'in-progress': return '🔄';
      case 'deferred': return '⏸️';
      default: return '❓';
    }
  }

  getPriorityColor(priority) {
    switch (priority) {
      case 'high': return '🔴';
      case 'medium': return '🟡';
      case 'low': return '🟢';
      default: return '⚪';
    }
  }

  showTask(taskId) {
    const tasks = this.loadTasks();
    const task = tasks.find(t => t.id == taskId);

    if (!task) {
      console.log(`Task ${taskId} not found.`);
      return;
    }

    console.log('\n📋 Task Details');
    console.log('=' .repeat(50));
    console.log(`ID: ${task.id}`);
    console.log(`Title: ${task.title}`);
    console.log(`Status: ${task.status}`);
    console.log(`Priority: ${task.priority}`);
    console.log(`Dependencies: ${task.dependencies?.join(', ') || 'None'}`);
    console.log('\nDescription:');
    console.log(task.description);
    console.log('\nDetails:');
    console.log(task.details);
    console.log('\nTest Strategy:');
    console.log(task.testStrategy);
  }

  updateTaskStatus(taskId, newStatus) {
    const tasks = this.loadTasks();
    const taskIndex = tasks.findIndex(t => t.id == taskId);

    if (taskIndex === -1) {
      console.log(`Task ${taskId} not found.`);
      return;
    }

    tasks[taskIndex].status = newStatus;

    try {
      const data = { meta: { projectName: "nextjs-template", version: "1.5.0" }, tasks };
      fs.writeFileSync(this.tasksPath, JSON.stringify(data, null, 2));
      console.log(`✅ Task ${taskId} status updated to: ${newStatus}`);
    } catch (error) {
      console.error('Error updating task:', error.message);
    }
  }

  showHelp() {
    console.log('\n📋 Task Master - WhatsApp Integration');
    console.log('=' .repeat(50));
    console.log('Commands:');
    console.log('  list                    - List all tasks');
    console.log('  list --status=pending   - List tasks by status');
    console.log('  show <id>               - Show task details');
    console.log('  update <id> <status>    - Update task status');
    console.log('  help                    - Show this help');
    console.log('\nStatus values: pending, in-progress, done, deferred');
    console.log('Priority values: high, medium, low');
  }
}

// Main execution
const args = process.argv.slice(2);
const taskMaster = new TaskMaster();

if (args.length === 0 || args[0] === 'help') {
  taskMaster.showHelp();
} else if (args[0] === 'list') {
  const statusFilter = args.find(arg => arg.startsWith('--status='))?.split('=')[1];
  taskMaster.listTasks(statusFilter);
} else if (args[0] === 'show' && args[1]) {
  taskMaster.showTask(args[1]);
} else if (args[0] === 'update' && args[1] && args[2]) {
  taskMaster.updateTaskStatus(args[1], args[2]);
} else {
  console.log('Unknown command. Use "help" to see available commands.');
}

