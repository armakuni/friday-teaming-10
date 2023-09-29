# Friday Teaming 10

For the 10th episode of Friday will try applying Object Calisthenics to The Banking Kata.

## The Banking Kata

[Source](https://kata-log.rocks/banking-kata)

### Credits
Inspired by Sandro Mancuso

### Your Task
Your bank is tired of its mainframe COBOL accounting software and they hired both of you for a greenfield project in - what a happy coincidence

* your favorite programming language!

Your task is to show them that your TDD-fu and your new-age programming language can cope with good ole’ COBOL!

### Requirements

Write a class Account that offers the following methods:

- `void deposit(int)`
- `void withdraw(int)`
- `String printStatement()`

An example statement would be:

```
Date        Amount  Balance
24/12/2015   +500      500
23/08/2016   -100      400
```

## The Banking Kata V2

### Requirements

Write a class Account that offers the following methods:

- `String createAccount(firstName: String, lastName: String, address1: String, address2: String, town: String,
   county: String, postCode: String)`
- `void deposit(accountId: String, amountPence: int)`
- `void withdraw(accountId: String, amountPence: int)`
- `String printStatement(accountId: String)`

An example statement would be:

```
Georgia Hutchinson
4 Shannon Way
Chippenham
Wiltshire
CB7 6UQ

Date        Amount  Balance
24/12/2015   +500      500
23/08/2016   -100      400
```

## Rules of Object Calisthenics

### 1. Only One Level of Indentation per Method

Any method should only have a maximum of one level of indentation.
If deeper indentation is needed then you should restructure or extract new
methods for the inner blocks.

#### Refactorings:

- [Extract method](https://refactoring.guru/extract-method)

#### Bad

```kotlin
fun assertAllSuccess(jobs: List<Job>) {
    for (job in jobs) {
        if (!job.success) {
            throw AssertionError("Job ${job.id} failed")
        }
    }
}
```

#### Good

```kotlin
fun assertAllSuccess(jobs: List<Job>) {
    for (job in jobs) {
        assertSuccess(job)
    }
}

fun assertSuccess(job: Job) {
    if (!job.success) {
        throw AssertionError("Job ${job.id} failed")
    }
}
```

### 2. Don't Use the ELSE Keyword

Find ways to remove the `else` keyword from your code.

#### Refactorings

- [Replace Nested Conditional with Guard Clauses](https://refactoring.guru/replace-nested-conditional-with-guard-clauses)

#### Bad

```kotlin
fun isSuccess(status: String): Boolean {
    var result: Boolean
    
    if (status != "success") {
        result = false
    } else {
        result = true
    }

    return result
}

```

#### Good

```kotlin
fun isSuccess(status: String): Boolean {
    var result = true

    if (status != "success") {
        result = false
    }

    return result
}

```

or

```kotlin
fun isSuccess(status: String): Boolean {
    if (status != "success") {
        return false
    }

    return true
}

```

or better yet (in this particular circumstance)

```kotlin
fun isSuccess(status: String): Boolean {
    return status == "success"
}

```

### 3. Wrap All Primitives and Strings

If you have a string, number or bool then wrap it in a class with a meaningful
type. Take the opportunity to add validation to the constructor of the class so
that it cannot be constructed with invalid values.

#### Bad

```kotlin
class User(val name: String, val age: Int) {
    init {
        if (age < 0) {
            throw IllegalArgumentException("Age must be positive")
        }
    }
}

```

#### Good

```kotlin
class PersonName(val name: String)

class Age(val age: Int) {
    init {
        if (age < 0) {
            throw IllegalArgumentException("Age must be positive")
        }
    }
}

class User(val name: PersonName, val age: Age)

```

### 4. First Class Collections

If you have an array or dictionary of values, wrap it in a class and create
methods for each specific operation you want to perform on it.

#### Bad

```kotlin
fun sendSpecialOfferEmail(users: List<User>, offer: String) {
    val subject = "Special Offer"
    val message = "Special offer: $offer"
    for (user in users) {
        sendEmail(user, subject, message)
    }
}

```

#### Good

```kotlin
class Users(val users: List<User>) {
    fun sendEmail(subject: String, message: String) {
        for (user in users) {
            sendEmail(user, subject, message)
        }
    }
}

fun sendSpecialOfferEmail(users: Users, offer: String) {
    val subject = "Special Offer"
    val message = "Special offer: $offer"
    users.sendEmail(subject, message)
}

```

### 5. One Dot per Line

One dot per line is actually misleading, what this is really referring to is
the [Law of Demeter](https://en.wikipedia.org/wiki/Law_of_Demeter). This states
that you can access a class's collaborators, but not a class's collaborators'
collaborators. In Python, we should call this "no more than two dots per line"
because you have to use `self`. (e.g. `self.collaborator.method()`).

#### Bad

```kotlin
class Order(val customer: Customer) {
    fun sendReceivedEmail(orderId: String) {
        customer.email.send("Order $orderId", "Your order has been received")
    }
}

```

#### Good

```kotlin
class Customer(val email: EmailAddress) {
    // ...

    fun sendEmail(subject: String, message: String) {
        email.send(subject, message)
    }
}

class Order(val customer: Customer) {
    fun sendReceivedEmail(orderId: String) {
        customer.sendEmail("Order $orderId", "Your order has been received")
    }
}

```

### 6. Don't Abbreviate

Just name things properly.

### 7. Keep All Entities Small

No class over **50 lines** long.

### 8. No Classes with More Than Two Instance Variables

If you have more than two instance variables, you have to group subsets together.
This can be a tough exercise in naming things!

#### Bad

```kotlin
class User(val name: PersonName, val email: Email, val phone: PhoneNumber)

```

#### Good

```kotlin
class ContactDetails(val email: Email, val phone: PhoneNumber)

class User(val name: PersonName, val contactDetails: ContactDetails)

```

### 9. No Getters/Setters/Properties

This may sound hard, but it's not so bad when you get the hang of it. It is the
idea of [Tell, Don't Ask](https://martinfowler.com/bliki/TellDontAsk.html).
Rather that asking objects to give you their internal state, you ask them to do
tasks for you and provide them with the dependencies they need. This works
especially well when the dependencies are defined as abstract interfaces, as it
makes the code more flexible.

#### Bad

```kotlin
fun sendEmail(customer: Customer) {
    val email = Email()
    email.toAddress = customer.emailAddress
    email.subject = "Your new order"
    email.body = "Dear ${customer.name}\nYour order has been received!"
    sendEmail(email)
}

```

#### Good

```kotlin
class Customer(val name: String, val emailAddress: String) {
    fun sendMessageTo(messenger: Messenger) {
        messenger.sendMessage(name, emailAddress)
    }
}

class OrderReceivedEmailer(val subject: String, val body: String) {
    fun sendMessage(name: String, emailAddress: String) {
        val email = Email(
            toAddress = emailAddress,
            subject = "Your new order",
            body = "Dear $name\nYour order has been received!"
        )
        sendEmail(email)
    }
}

fun sendEmail(customer: Customer) {
    val messenger = OrderReceivedEmailer("Your new order", "")
    customer.sendMessageTo(messenger)
}

```
