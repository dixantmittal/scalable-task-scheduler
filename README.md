# Scalable Task Scheduler

A good Task Scheduler is a requirement of every organitszation. 
But most task schedulers fell short when scaling is concerned and when each Task starts taking time to execute.
For example, Quartz, when the execution time of jobs starts increasing, execution threads start choking. 
In comes this task scheduler in which the execution part and scheduing part are decoupled using Kafka.
Due to Kafka acting as a bridge between scheduler and executor, Executors can be increased or decreased any time.

## Getting Started

This project has 3 main modules: TaskSchedulerClient, TaskManager and TaskExecutor. They work as separate application.
TaskManager manages a task. All the add new task and delete task are handled by it. This is a web service and the scheduler can be paused if required via API.
TaskSchedulerClient is a client for TaskManager. It is a simple jar that you need to inlcude in your project which wants to scheduler a task. Simply add its configuration and start scheduling tasks.
TaskExecutor are the executor threads. Each application will act as a new executor thread. So whenever required, you can easily start or stop application to upscale or downscale the executor.

### Prerequisites

* JRE,
* MySQL (also need to add some predefined tables in the db),
* Kafka

## Deployment

First you need to create a Kafka Topic for a Task Type. For that task type, you need to enter a value in the configuration table in the db. You'll need to extend AbstractTaskExecutor class and write logic for the task execution. Then specify which class represents the execution class for a task type. Now start the TaskManager service and create few TaskExecutor threads for that task type.
Configure TaskSchedulerClient jar with addresses of Kafka topic. After that this jar will start scheduling tasks in the TaskManager which will pick up the task at right time and send it to execution thread.

## Built With

* [Spring Boot](https://projects.spring.io/spring-boot/) - The framework used
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Dixant Mittal** - *Initial work* - [dixantmittal](https://github.com/dixantmittal)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
