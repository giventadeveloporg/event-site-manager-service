#!/usr/bin/env node

import fs from 'fs';
import path from 'path';

// Simple task lister that doesn't require external dependencies
function listTasks() {
  try {
    const tasksPath = '.task-master/tasks/tasks.json';
    const data = JSON.parse(fs.readFileSync(tasksPath, 'utf8'));

    if (!data || !data.tasks) {
      console.log('No tasks found.');
      return;
    }

    console.log('\n📋 Task Master - Task List');
    console.log('=' .repeat(50));
    console.log(`Project: ${data.meta.projectName}`);
    console.log(`Total Tasks: ${data.meta.totalTasksGenerated}`);
    console.log('=' .repeat(50));

    data.tasks.forEach(task => {
      const status = task.status === 'pending' ? '⏳' :
                   task.status === 'done' ? '✅' :
                   task.status === 'in-progress' ? '🔄' : '❓';

      console.log(`${status} [${task.id}] ${task.title}`);
      console.log(`    Status: ${task.status} | Priority: ${task.priority}`);
      if (task.dependencies && task.dependencies.length > 0) {
        console.log(`    Dependencies: ${task.dependencies.join(', ')}`);
      }
      console.log('');
    });

  } catch (error) {
    console.error('Error reading tasks:', error.message);
  }
}

listTasks();

