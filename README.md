# Task Tracker - CLI

## Inspiration

<https://roadmap.sh/projects/task-tracker>

### Tools & Tech

- Jackson
- Maven
- JUnit5, Mockito
- Java 24

## How to use

### Add

```zsh
$ java -jar task-tracker-1.0.jar add "my first task yay"
69cd46ac-6019-489e-ac34-d6708fbb1c14
$ java -jar task-tracker-1.0.jar list
[69cd46ac-6019-489e-ac34-d6708fbb1c14] todo my first task yay
```

### Update

#### Update Description

```zsh
$ java -jar task-tracker-1.0.jar update 69cd46ac-6019-489e-ac34-d6708fbb1c14 "medium priority task"
$ java -jar task-tracker-1.0.jar list
[69cd46ac-6019-489e-ac34-d6708fbb1c14] todo medium priority task
```

#### Update Status

```zsh
$ java -jar task-tracker-1.0.jar mark-in-progress 69cd46ac-6019-489e-ac34-d6708fbb1c14
$ java -jar task-tracker-1.0.jar list
[69cd46ac-6019-489e-ac34-d6708fbb1c14] in-progress task1

$ java -jar task-tracker-1.0.jar mark-done 060fc220-3714-4138-b47a-780dd0dd1d01
$ java -jar task-tracker-1.0.jar list
[69cd46ac-6019-489e-ac34-d6708fbb1c14] in-progress task1
[060fc220-3714-4138-b47a-780dd0dd1d01] done must do second task
```

### Delete

```zsh
$ java -jar task-tracker-1.0.jar delete 060fc220-3714-4138-b47a-780dd0dd1d01
$ java -jar task-tracker-1.0.jar list
[69cd46ac-6019-489e-ac34-d6708fbb1c14] in-progress task1
```
