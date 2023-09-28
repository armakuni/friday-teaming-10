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

Write a class Account that offers the following methods void deposit(int) void withdraw(int) String printStatement()

An example statement would be:

```
Date        Amount  Balance
24.12.2015   +500      500
23.8.2016    -100      400
```

## Rules of Object Calisthenics

### 1. Only One Level of Indentation per Method

Any method should only have a maximum of one level of indentation.
If deeper indentation is needed then you should restructure or extract new
methods for the inner blocks.

#### Refactorings:

- [Extract method](https://refactoring.guru/extract-method)

#### Bad

```python
def assert_all_success(jobs):
    for job in jobs:
        if not job.success:
            raise AssertionError("Job %s failed" % job.id)
```

#### Good

```python
def assert_all_success(jobs):
    for job in jobs:
        assert_success(job)


def assert_success(job):
    if not job.success:
        raise AssertionError("Job %s failed" % job.id)
```

### 2. Don't Use the ELSE Keyword

Find ways to remove the `else` keyword from your code.

Note: `elif` is also `else`.

#### Refactorings

- [Replace Nested Conditional with Guard Clauses](https://refactoring.guru/replace-nested-conditional-with-guard-clauses)

#### Bad

```python
def is_success(self):
    if self.status != "success":
        result = False
    else:
        result = True

    return result
```

#### Good

```python
def is_success(self):
    result = True

    if self.status != "success":
        result = False

    return result
```

or

```python
def is_success(self):
    if self.status != "success":
        return False

    return True
```

or better yet (in this particular circumstance)

```python
def is_success(self):
    return self.status == "success"
```

### 3. Wrap All Primitives and Strings

If you have a string, number or bool then wrap it in a class with a meaningful
type. Take the opportunity to add validation to the constructor of the class so
that it cannot be constructed with invalid values.

#### Bad

```python
class User:
    def __init__(self, name: str, age: str):
        if age < 0:
            raise ValueError("Age must be positive")

        self.name = name
        self.age = age
```

#### Good

```python
class PersonName:
    def __init__(self, name: str):
        self.name = name


class Age:
    def __init__(self, age: int):
        if age < 0:
            raise ValueError("Age must be positive")

        self.age = age


class User:
    def __init__(self, name: PersonName, age: Age):
        self.name = name
        self.age = age
```

### 4. First Class Collections

If you have an array or dictionary of values, wrap it in a class and create
methods for each specific operation you want to perform on it.

#### Bad

```python
def send_special_offer_email(users, offer: str):
    subject = "Special Offer"
    message = "Special offer: %s" % offer
    for user in users:
        send_email(user, subject, message)
```

#### Good

```python
class Users:
    def __init__(self, users: List[User]):
        self.users = users

    def send_email(self, subject, message):
        for user in self.users:
            send_email(user, subject, message)


def send_special_offer_email(users: Users, offer: str):
    subject = "Special Offer"
    message = "Special offer: %s" % offer
    users.send_email(subject, message)
```

### 5. One Dot per Line

One dot per line is actually misleading, what this is really referring to is
the [Law of Demeter](https://en.wikipedia.org/wiki/Law_of_Demeter). This states
that you can access a class's collaborators, but not a class's collaborators'
collaborators. In Python, we should call this "no more than two dots per line"
because you have to use `self`. (e.g. `self.collaborator.method()`).

#### Bad

```python
class Order:
    def send_received_email(self):
        self.customer.email.send("Order %s" % self.order_id, "Your order has been received")
```

#### Good

```python
class Customer:
    # ...

    def send_email(self, subject, message):
        self.email.send(subject, message)


class Order:
    def send_received_email(self):
        self.customer.send_email("Order %s" % self.order_id, "Your order has been received")
```

### 6. Don't Abbreviate

Just name things properly.

### 7. Keep All Entities Small

No class over **50 lines** long.

### 8. No Classes with More Than Two Instance Variables

If you have more than two instance variables, you have to group subsets together.
This can be a tough exercise in naming things!

#### Bad

```python
class User:
    def __init__(self, name: PersonName, email: Email, phone: PhoneNumber):
        self.name = name
        self.email = email
        self.phone = phone
```

#### Good

```python
class ContactDetails:
    def __init__(self, email: Email, phone: PhoneNumber):
        self.email = email
        self.phone = phone


class User:
    def __init__(self, name: PersonName, contact_details: ContactDetails):
        self.name = name
        self.contact_details = contact_details
```

### 9. No Getters/Setters/Properties

This may sound hard, but it's not so bad when you get the hang of it. It is the
idea of [Tell, Don't Ask](https://martinfowler.com/bliki/TellDontAsk.html).
Rather that asking objects to give you their internal state, you ask them to do
tasks for you and provide them with the dependencies they need. This works
especially well when the dependencies are defined as abstract interfaces, as it
makes the code more flexible.

#### Bad

```python
def send_email(customer):
    email = Email()
    email.to_address = customer.email_address
    email.subject = "Your new order"
    email.body = (
        f"Dear {customer.name}\n"
        f"Your order has been received!"
    )
    send_email(email)
```

#### Good

```python
class Customer:
    def __init__(self, name: str, email_address: str):
        self.name = name
        self.email_address = email_address

    def send_message_to(self, messenger: Messenger):
        messenger.send_message(self.name, self.email_address)


class OrderReceivedEmailer:
    def __init__(self, subject: str, body: str):
        self.subject = subject
        self.body = body

    def send_message(self, name: str, email_address: str):
        email = Email(
            to_address=customer.email_address,
            subject="Your new order",
            body=(
                f"Dear {customer.name}\n"
                f"Your order has been received!"
            )
        )
        send_email(email)


def send_email(customer):
    messenger = OrderReceivedEmailer()
    customer.send_message_to(messenger)
```
