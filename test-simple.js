#!/usr/bin/env node

console.log('Hello from Node.js!');
console.log('Testing task-master system...');

try {
  const fs = require('fs');
  const tasksPath = '.task-master/tasks/tasks.json';

  if (fs.existsSync(tasksPath)) {
    console.log('✅ Tasks file found');
    const data = JSON.parse(fs.readFileSync(tasksPath, 'utf8'));
    console.log(`Found ${data.tasks?.length || 0} tasks`);
  } else {
    console.log('❌ Tasks file not found');
  }
} catch (error) {
  console.error('Error:', error.message);
}

