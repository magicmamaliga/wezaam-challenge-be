### Technical challenge (for developers)

Assume we have an extensive legacy system and one of the parts is withdrawal processing (the process that allows to
transfer money from company to employee accounts). Now we have a chance to completely rewrite the system, including API
change (endpoints, DTOs etc). As a technical challenge, we suggest you to take it. You can do whatever you want
following the acceptance criteria:

- Use any architecture you are comfortable with
- Use modern Java or Kotlin (we use Kotlin for new code)
- Use any database SQL/NoSQL (please use embedded)
- The code must be tested. We don't expect 100% coverage for this challenge, we want to see that you can write sensible
  tests. For example, if you have several similar converters, there is no need to test every single class/method, just
  enough to test one. But for critical logic like the withdrawal process we'd like to see coverage of different
  scenarios
- We expect to see SOLID principles in action

#### Here are some business rules for withdrawal processing:

- We have a list of users (`/find-all-users` endpoint)
- A user has several payment methods
- A user can execute a withdrawal request using one of his payment methods
- A withdrawal can be executed as soon as possible (note: it doesn't mean immediately) or scheduled to be executed later
- After the service receives a request it should:
    - Store a withdrawal object in a DB in pending status
    - Send an event to a queue (please just emulate it, don't use a real queue like Kafka, Rabbit)
    - Send a transaction request to a payment provider async. Note: for this task, we don't care about transaction
      completion
    - Return the pending withdrawal

#### Steps to proceed:

- Fork the repository (in case you want to refactor the existing solution). Or you can create the project from scratch
- Implement your nice solution
- If you need to put comments/description regarding the solution please write them in `SOLUTION.md`

###### As a bonus please describe (or implement a solution) how to solve the problem:

We noticed that in the current solution, we are losing some outgoing events about withdrawals. We MUST 100% notify
listeners regarding any withdrawal statuses. For example: in `WithdrawalService.processScheduled`method, we updated the
status to processing in the database (line 89), and then we sent an event (line 90). What if the event failed to send (
e.q. connection issues to a messaging provider)?
