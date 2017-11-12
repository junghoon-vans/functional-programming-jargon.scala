# Scala code examples for Functional Programming Jargon 

- This project is fork of [hemanth/functional-programming-jargon](https://github.com/hemanth/functional-programming-jargon)

# Functional Programming Jargon

Functional programming (FP) provides many advantages, and its popularity has been increasing as a result. However, each programming paradigm comes with its own unique jargon and FP is no exception. By providing a glossary, we hope to make learning FP easier.

Examples are presented in JavaScript (ES2015). [Why JavaScript?](https://github.com/hemanth/functional-programming-jargon/wiki/Why-JavaScript%3F)

*This is a [WIP](https://github.com/hemanth/functional-programming-jargon/issues/20); please feel free to send a PR ;)*

Where applicable, this document uses terms defined in the [Fantasy Land spec](https://github.com/fantasyland/fantasy-land)

__Translations__
* [Portuguese](https://github.com/alexmoreno/jargoes-programacao-funcional)
* [Spanish](https://github.com/idcmardelplata/functional-programming-jargon/tree/master)
* [Chinese](https://github.com/shfshanyue/fp-jargon-zh)
* [Bahasa Indonesia](https://github.com/wisn/jargon-pemrograman-fungsional)

__Table of Contents__
<!-- RM(noparent,notop) -->

* [Arity](#arity)
* [Higher-Order Functions (HOF)](#higher-order-functions-hof)
* [Partial Application](#partial-application)
* [Currying](#currying)
* [Closure](#closure)
* [Auto Currying](#auto-currying)
* [Function Composition](#function-composition)
* [Continuation](#continuation)
* [Purity](#purity)
* [Side effects](#side-effects)
* [Idempotent](#idempotent)
* [Point-Free Style](#point-free-style)
* [Predicate](#predicate)
* [Contracts](#contracts)
* [Category](#category)
* [Value](#value)
* [Constant](#constant)
* [Functor](#functor)
* [Pointed Functor](#pointed-functor)
* [Lift](#lift)
* [Referential Transparency](#referential-transparency)
* [Equational Reasoning](#equational-reasoning)
* [Lambda](#lambda)
* [Lambda Calculus](#lambda-calculus)
* [Lazy evaluation](#lazy-evaluation)
* [Monoid](#monoid)
* [Monad](#monad)
* [Comonad](#comonad)
* [Applicative Functor](#applicative-functor)
* [Morphism](#morphism)
  * [Endomorphism](#endomorphism)
  * [Isomorphism](#isomorphism)
* [Setoid](#setoid)
* [Semigroup](#semigroup)
* [Foldable](#foldable)
* [Lens](#lens)
* [Type Signatures](#type-signatures)
* [Algebraic data type](#algebraic-data-type)
  * [Sum type](#sum-type)
  * [Product type](#product-type)
* [Option](#option)
* [Functional Programming Libraries in Scala](#functional-programming-libraries-in-scala)


<!-- /RM -->

## Arity

The number of arguments a function takes. From words like unary, binary, ternary, etc. This word has the distinction of being composed of two suffixes, "-ary" and "-ity." Addition, for example, takes two arguments, and so it is defined as a binary function or a function with an arity of two. Such a function may sometimes be called "dyadic" by people who prefer Greek roots to Latin. Likewise, a function that takes a variable number of arguments is called "variadic," whereas a binary function must be given two and only two arguments, currying and partial application notwithstanding (see below).

```tut:book
val sum = (a : Int, b: Int) => a + b // The arity of sum is 2
```

## Higher-Order Functions (HOF)

A function which takes a function as an argument and/or returns a function.


```tut:book
val filter = (predicate: Int => Boolean, xs: List[Int]) => xs.filter(predicate)
```
```tut:book
val isEven = (x: Int) => x % 2 == 0
```

```tut:book
filter(isEven, List(1, 2, 3, 4, 5))
```

## Partial Application

Partially applying a function means creating a new function by pre-filling some of the arguments to the original function.


```tut:book
// Something to apply
val add3 = (a: Int, b: Int, c: Int) => a + b + c

// Partially applying `2` and `3` to `add3` gives you a one-argument function
val fivePlus = add3(2, 3, _: Int) // (c) => 2 + 3 + c

fivePlus(4)
```

Partial application helps create simpler functions from more complex ones by baking in data when you have it. [Curried](#currying) functions are automatically partially applied.

## Currying

The process of converting a function that takes multiple arguments into a function that takes them one at a time.

Each time the function is called it only accepts one argument and returns a function that takes one argument until all arguments are passed.

```tut:book
val sum = (a : Int, b: Int) => a + b

val curriedSum = (a: Int) => (b: Int) => a + b

curriedSum(40)(2) // 42.

val add2 = curriedSum(2) // (b) => 2 + b

add2(10) // 12

```

## Closure

A closure is a way of accessing a variable outside its scope.
Formally, a closure is a technique for implementing lexically scoped named binding. It is a way of storing a function with an environment.

A closure is a scope which captures local variables of a function for access even after the execution has moved out of the block in which it is defined.
ie. they allow referencing a scope after the block in which the variables were declared has finished executing.


```tut:book
val addTo = (x :Int) => (y: Int) => x + y
val addToFive = addTo(6)
addToFive(3) // returns 8
```

The function `addTo()` returns a function(internally called `add()`), lets store it in a variable called `addToFive` with a curried call having parameter 5.

Ideally, when the function `addTo` finishes execution, its scope, with local variables add, x, y should not be accessible. But, it returns 8 on calling `addToFive()`. This means that the state of the function `addTo` is saved even after the block of code has finished executing, otherwise there is no way of knowing that `addTo` was called as `addTo(5)` and the value of x was set to 5.

Lexical scoping is the reason why it is able to find the values of x and add - the private variables of the parent which has finished executing. This value is called a Closure.

The stack along with the lexical scope of the function is stored in form of reference to the parent. This prevents the closure and the underlying variables from being garbage collected(since there is at least one live reference to it).

Lambda Vs Closure: A lambda is essentially a function that is defined inline rather than the standard method of declaring functions. Lambdas can frequently be passed around as objects.

A closure is a function that encloses its surrounding state by referencing fields external to its body. The enclosed state remains across invocations of the closure.


__Further reading/Sources__
* [Lambda Vs Closure](http://stackoverflow.com/questions/220658/what-is-the-difference-between-a-closure-and-a-lambda)
* [How do JavaScript Closures Work?](http://stackoverflow.com/questions/111102/how-do-javascript-closures-work)

## Auto Currying
Transforming a function that takes multiple arguments into one that if given less than its correct number of arguments returns a function that takes the rest. When the function gets the correct number of arguments it is then evaluated.

lodash & Ramda have a `curry` function that works this way.

```tut:book
val add = (x: Int, y: Int) => x + y

val curriedAdd = add.curried
curriedAdd(2) // (y) => 1 + y
curriedAdd(1)(2) // 3
```

__Further reading__
* [Favoring Curry](http://fr.umio.us/favoring-curry/)
* [Hey Underscore, You're Doing It Wrong!](https://www.youtube.com/watch?v=m3svKOdZijA)

## Function Composition

The act of putting two functions together to form a third function where the output of one function is the input of the other.

```tut:book
def compose[A, B, C](f: B => C, g: A => B) = (a: A) => f(g(a)) // Definition
val floorAndToString = compose((x: Double) => x.toString, math.floor) // Usage
floorAndToString(121.212121) // '121.0'
```

## Continuation

At any given point in a program, the part of the code that's yet to be executed is known as a continuation.

```tut:book
def printAsString(num: Int) = println(s"Given $num")
val printAsString= (num: Int) => println(s"Given $num")
val addOneAndContinue = (num: Int, cc: Int => Any) => {
  val result = num + 1
  cc(result)
}

addOneAndContinue(2, printAsString) 
```

Continuations are often seen in asynchronous programming when the program needs to wait to receive data before it can continue. The response is often passed off to the rest of the program, which is the continuation, once it's been received.

```tut:book
def continueProgramWith(data: String) = {
  // Continues program with data
}

def readFileAsync(file: String, cb: (Option[Throwable], String) => Unit) = {}

readFileAsync("path/to/file", (err, response) => {
  if (err.isDefined) {
    // handle error
    ()
  }
  continueProgramWith(response)
})
```

## Purity

A function is pure if the return value is only determined by its
input values, and does not produce side effects.

```tut:book
val greet = (name: String) => s"Hi, ${name}"

greet("Brianne")
```

As opposed to each of the following:

```tut:book
var name = "Brianne"

def greet = () => s"Hi, ${name}"

greet() 
```

The above example's output is based on data stored outside of the function...

```tut:book
var greeting: String = _

val greet = (name: String) => {
  greeting = s"Hi, ${name}"
}

greet("Brianne")
greeting 
```

... and this one modifies state outside of the function.

## Side effects

A function or expression is said to have a side effect if apart from returning a value, it interacts with (reads from or writes to) external mutable state.

```tut:book
import java.util.Date
def differentEveryTime = new Date()
```

```tut:book
println("IO is a side effect!")
```

## Idempotent

A function is idempotent if reapplying it to its result does not produce a different result.

```
f(f(x)) ≍ f(x)
```

```tut:book
math.abs(math.abs(10))
```

```tut:book
def sort[A: Ordering](xs: List[A]) = xs.sorted
sort(sort(sort(List(2, 1))))
```

## Point-Free Style

Writing functions where the definition does not explicitly identify the arguments used. This style usually requires [currying](#currying) or other [Higher-Order functions](#higher-order-functions-hof). A.K.A Tacit programming.

```tut:book
// Given
def map[A, B](fn: A => B) = (list: List[A]) => list.map(fn)
val add = (a: Int) => (b: Int) => a + b

// Then

// Not points-free - `numbers` is an explicit argument
val incrementAll = (numbers: List[Int]) => map(add(1))(numbers)

// Points-free - The list is an implicit argument
val incrementAll2 = map(add(1))
```

`incrementAll` identifies and uses the parameter `numbers`, so it is not points-free.  `incrementAll2` is written just by combining functions and values, making no mention of its arguments.  It __is__ points-free.

Points-free function definitions look just like normal assignments without `function` or `=>`.

## Predicate
A predicate is a function that returns true or false for a given value. A common use of a predicate is as the callback for array filter.

```tut:book
val predicate = (a: Int) => a > 2

List(1, 2, 3, 4).filter(predicate)
```

## Contracts

A contract specifies the obligations and guarantees of the behavior from a function or expression at runtime. This acts as a set of rules that are expected from the input and output of a function or expression, and errors are generally reported whenever a contract is violated.

```tut:nofail
// Define our contract : int -> int
def contract[A](input: A): Boolean = input match {
  case _ : Int => true 
  case _ => throw new Exception("Contract violated: expected int -> int")
}

def addOne[A](num: A): Int = {
  contract(num)
  num.asInstanceOf[Int] + 1
}

addOne(2) // 3
addOne("some string") // Contract violated: expected int -> int
```

## Category

A category in category theory is a collection of objects and morphisms between them. In programming, typically types
act as the objects and functions as morphisms.

To be a valid category 3 rules must be met:

1. There must be an identity morphism that maps an object to itself.
    Where `a` is an object in some category,
    there must be a function from `a -> a`.
2. Morphisms must compose.
    Where `a`, `b`, and `c` are objects in some category,
    and `f` is a morphism from `a -> b`, and `g` is a morphism from `b -> c`;
    `g(f(x))` must be equivalent to `(g • f)(x)`.
3. Composition must be associative
    `f • (g • h)` is the same as `(f • g) • h`

Since these rules govern composition at very abstract level, category theory is great at uncovering new ways of composing things.

__Further reading__

* [Category Theory for Programmers](https://bartoszmilewski.com/2014/10/28/category-theory-for-programmers-the-preface/)

## Value

Anything that can be assigned to a variable.

```tut:book
case class Person(name: String, age: Int)
5
Person("John", 30) 
(a: Any) => a
List(1)
null
```

## Constant

A variable that cannot be reassigned once defined.

```tut:book
val five = 5
val john = Person("John", 30)
```

Constants are [referentially transparent](#referential-transparency). That is, they can be replaced with the values that they represent without affecting the result.

With the above two constants the following expression will always return `true`.

```tut:book
john.age + five == Person("John", 30).age + 5
```

## Functor

An object that implements a `map` function which, while running over each value in the object to produce a new object, adheres to two rules:

__Preserves identity__
```
object.map(x => x) ≍ object
```

__Composable__

```
object.map(compose(f, g)) ≍ object.map(g).map(f)
```

(`f`, `g` are arbitrary functions)

A common functor in JavaScript is `Array` since it abides to the two functor rules:

```tut:book
List(1, 2, 3).map(x => x) 
```

and

```tut:book
val f = (x: Int) => x + 1
val g = (x: Int) => x * 2

List(1, 2, 3).map(x => f(g(x)))
List(1, 2, 3).map(g).map(f)    
```

## Pointed Functor
An Applicative with an `pure` function that puts _any_ single value into it.

[cats](https://typelevel.org/cats/) adds [`Applicative#pure`](https://github.com/typelevel/cats/blob/31874ce42b7e98bbb436a430c6a708f8e9db0e6d/core/src/main/scala/cats/Applicative.scala#L23) making arrays a pointed functor.

```tut:book
import cats._
import cats.implicits._
Applicative[List].pure(1)
```

## Lift

Lifting is when you take a value and put it into an object like a [functor](#pointed-functor). If you lift a function into an [Applicative Functor](#applicative-functor) then you can make it work on values that are also in that functor.

Some implementations have a function called `lift`, or `liftA2` to make it easier to run functions on functors.

```tut:book
def liftA2[F[_]: Monad, A, B, C](f: A => B => C)(a: F[A], b: F[B]) = {
  a.map(f).ap(b)
}

val mult = (a: Int) => (b: Int) => a * b

val liftedMult = liftA2[List, Int, Int, Int](mult) _

liftedMult(List(1, 2), List(3))
liftA2((a: Int) => (b: Int) => a + b)(List(1, 2), List(3, 4))
```

Lifting a one-argument function and applying it does the same thing as `map`.

```tut:book
val increment = (x: Int) => x + 1

Applicative[List].lift(increment)(List(2))
List(2).map(increment)
```


## Referential Transparency

An expression that can be replaced with its value without changing the
behavior of the program is said to be referentially transparent.

Say we have function greet:

```tut:book
val greet = () => "Hello World!"
```

Any invocation of `greet()` can be replaced with `Hello World!` hence greet is
referentially transparent.

##  Equational Reasoning

When an application is composed of expressions and devoid of side effects, truths about the system can be derived from the parts.

## Lambda

An anonymous function that can be treated like a value.

```tut:book
(_: Int) + 1

(x: Int) => x + 1
```
Lambdas are often passed as arguments to Higher-Order functions.

```tut:book
List(1, 2).map(_ + 1)
```

You can assign a lambda to a variable.

```tut:book
val add1 = (a: Int) => a + 1
```

## Lambda Calculus
A branch of mathematics that uses functions to create a [universal model of computation](https://en.wikipedia.org/wiki/Lambda_calculus).

## Lazy evaluation

Lazy evaluation is a call-by-need evaluation mechanism that delays the evaluation of an expression until its value is needed. In functional languages, this allows for structures like infinite lists, which would not normally be available in an imperative language where the sequencing of commands is significant.

```tut:book
lazy val rand: Double = {
  println("generate random value...")
  math.random()
}
```

```tut:book
rand // Each execution gives a random value, expression is evaluated on need.
```

## Monoid

An object with a function that "combines" that object with another of the same type.

One simple monoid is the addition of numbers:

```tut:book
1 + 1 
```
In this case number is the object and `+` is the function.

An "identity" value must also exist that when combined with a value doesn't change it.

The identity value for addition is `0`.
```tut:book
1 + 0 
```

It's also required that the grouping of operations will not affect the result (associativity):

```tut:book
1 + (2 + 3) == (1 + 2) + 3
```

Array concatenation also forms a monoid:

```tut:book
List(1, 2) ::: List(3, 4)
```

The identity value is empty array `[]`

```tut:book
List(1, 2) ::: List()
```

If identity and compose functions are provided, functions themselves form a monoid:

```tut:book
def identity[A](a: A): A = a

def compose[A, B, C](f: B => C, g: A => B) = (a: A) => f(g(a)) // Definition
```
`foo` is any function that takes one argument.
```
compose(foo, identity) ≍ compose(identity, foo) ≍ foo
```

## Monad

A monad is an object with [`pure`](#pointed-functor) and `flatMap` functions. `flatMap` is like [`map`](#functor) except it un-nests the resulting nested object.

```tut:book
// Implementation
trait Monad[F[_]] {
  def pure[A](a: A): F[A]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  def map[A, B](fa: F[A])(f: A => B): F[B]
}
object Monad {
  def apply[F[_]](implicit ev: Monad[F]) = ev
  
  implicit val listInstance: Monad[List] = new Monad[List] {
    def pure[A](x: A) = List(x)

    def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = 
      fa.foldLeft(List[B]()) { (acc, x) => acc ::: f(x) }
      
    def map[A, B](fa: List[A])(f: A => B): List[B] =
      flatMap(fa)(x => pure(f(x)))
      
  }
}

import Monad._
// Usage
Monad[List].flatMap(List("cat,dog", "fish,bird"))(a => a.split(",").toList)

// Contrast to map
Monad[List].map(List("cat,dog", "fish,bird"))(a => a.split(",").toList)
```

`pure` is also known as `return` in other functional languages.
`flatMap` is also known as `bind` in other languages.

## Comonad

An object that has `extract` and `coflatMap` functions.

```tut:book
trait Comonad[F[_]] {
  def extract[A](x: F[A]): A
  def coflatMap[A, B](fa: F[A])(f: F[A] => B): F[B]
}

type Id[X] = X
def id[X](x: X): Id[X] = x

object Comonad {
  def apply[F[_]](implicit ev: Comonad[F]) = ev

  implicit val idInstance: Comonad[Id] = new Comonad[Id] {
    def extract[A](x: Id[A]): A = x
    def coflatMap[A, B](fa: Id[A])(f: Id[A] => B): Id[B] = {
      id(f(fa))
    }
  }
}
```

Extract takes a value out of a functor.

```tut:book
import Comonad._
Comonad[Id].extract(id(1))
```

Extend runs a function on the comonad. The function should return the same type as the comonad.

```tut:book
Comonad[Id].coflatMap[Int, Int](id(1))(co => Comonad[Id].extract(co) + 1)
```

## Applicative Functor

An applicative functor is an object with an `ap` function. `ap` applies a function in the object to a value in another object of the same type.

```tut:book
// Implementation
trait Applicative[F[_]] {
  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
}


object Applicative {
  def apply[F[_]](implicit ev: Applicative[F]) = ev
  
  implicit val listInstance = new Applicative[List] {
    def ap[A, B](ff: List[A => B])(fa: List[A]): List[B] =
      ff.foldLeft(List[B]()) { (acc, f) => acc ::: fa.map(f) }
  }
}

import Applicative._

// Example usage
Applicative[List].ap(List((_: Int) + 1))(List(1))
```

This is useful if you have two objects and you want to apply a binary function to their contents.

```tut:book
// Arrays that you want to combine
val arg1 = List(1, 3)
val arg2 = List(4, 5)

// combining function - must be curried for this to work
val add = (x: Int) => (y: Int) => x + y

val partiallyAppiedAdds = Applicative[List].ap(List(add))(arg1) // [(y) => 1 + y, (y) => 3 + y]
```

This gives you an array of functions that you can call `ap` on to get the result:

```tut:book
Applicative[List].ap(partiallyAppiedAdds)(arg2)
```

## Morphism

A transformation function.

### Endomorphism

A function where the input type is the same as the output.

```tut:book
// uppercase :: String -> String
val uppercase = (str: String) => str.toUpperCase

// decrement :: Number -> Number
val decrement = (x: Int) => x - 1
```

### Isomorphism

A pair of transformations between 2 types of objects that is structural in nature and no data is lost.

For example, 2D coordinates could be stored as an array `[2,3]` or object `{x: 2, y: 3}`.

```tut:book
// Providing functions to convert in both directions makes them isomorphic.
case class Coords(x: Int, y: Int)

val pairToCoords = (pair: (Int, Int)) => Coords(pair._1, pair._2)

val coordsToPair = (coods: Coords) => (coods.x, coods.y)

coordsToPair(pairToCoords((1, 2)))

pairToCoords(coordsToPair(Coords(1, 2)))
```



## Setoid

An object that has an `equals` function which can be used to compare other objects of the same type.

Make array a setoid:

```tut:book
trait Eq[A] {
  def eqv(x: A, y: A): Boolean
}

object Eq {
  def apply[A](implicit ev: Eq[A]) = ev

  implicit def arrayInstance[B]: Eq[Array[B]] = new Eq[Array[B]] {
    def eqv(xs: Array[B], ys: Array[B]): Boolean =
      xs.zip(ys).foldLeft(true) {
        case (isEq, (x, y)) => isEq && x == y
      }
  }
  
  implicit class EqOps[A](x: A) {
    def eqv(y: A)(implicit ev: Eq[A]) =
      ev.eqv(x, y)
  }
}


import Eq._

Array(1, 2) == Array(1, 2)
Array(1, 2).eqv(Array(1, 2))
Array(1, 2).eqv(Array(0))
```

## Semigroup

An object that has a `combine` function that combines it with another object of the same type.

```tut:book
trait Semigroup[A] {
  def combine(x: A, y: A): A
}
object Semigroup {
  def apply[A](implicit ev: Semigroup[A]) = ev

  implicit def listInstance[B]: Semigroup[List[B]] = new Semigroup[List[B]] {
    def combine(x: List[B], y: List[B]): List[B] = x ::: y
  }

}

import Semigroup._

Semigroup[List[Int]].combine(List(1), List(2))
```

## Foldable

An object that has a `reduce` function that can transform that object into some other type.

```tut:book
def sum[A](xs: List[A])(implicit N: Numeric[A]) : A =
  Foldable[List].foldLeft(xs, N.zero) {
    case (acc, x) => N.plus(acc, x)
  }

sum(List(1, 2, 3))
```

## Lens ##
A lens is a structure (often an object or function) that pairs a getter and a non-mutating setter for some other data
structure.

```tut:book
// Using [Monocle's lens](https://github.com/julien-truffaut/Monocle)
import monocle.Lens
case class Person(name: String)

val nameLens = Lens[Person, String](_.name)(str => p => p.copy(name = str))
```

Having the pair of get and set for a given data structure enables a few key features.

```tut:book
val person = Person("Gertrude Blanch")

// invoke the getter
nameLens.get(person)

// invoke the setter
nameLens.set("Shafi Goldwasser")(person)

// run a function on the value in the structure
nameLens.modify(_.toUpperCase)(person)
```

Lenses are also composable. This allows easy immutable updates to deeply nested data.

```tut:book
// This lens focuses on the first item in a non-empty array

def firstLens[A] = Lens[List[A], A] {
  // get first item in array
  _.head
} { 
  // non-mutating setter for first item in array
  x => xs => x :: xs.tail
}


val people = List(Person("Gertrude Blanch"), Person("Shafi Goldwasser"))

// Despite what you may assume, lenses compose left-to-right.
(firstLens composeLens nameLens).modify(_.toUpperCase)(people)
```

Other implementations:
* [Quicklens](https://github.com/adamw/quicklens) - Modify deeply nested case class fields
* [Sauron](https://github.com/pathikrit/sauron) - Yet another Scala lens macro, Lightweight lens library in less than 50-lines of Scala
* [scalaz.Lens](http://eed3si9n.com/learning-scalaz/Lens.html)
                                                                                


## Type Signatures 

Every functions in Scala will indicate the types of their arguments and return values.

```tut:book
// functionName :: firstArgType -> secondArgType -> returnType

// add :: Number -> Number -> Number
val add = (x: Int) => (y: Int) => x + y

// increment :: Number -> Number
val increment = (x: Int) => x + 1
```

If a function accepts another function as an argument it is wrapped in parentheses.

```tut:book
// call :: (a -> b) -> a -> b
def call[A, B] = (f: A => B) => (x: A) => f(x)
```

The letters `a`, `b`, `c`, `d` are used to signify that the argument can be of any type. The following version of `map` takes a function that transforms a value of some type `a` into another type `b`, an array of values of type `a`, and returns an array of values of type `b`.

```tut:book
// map :: (a -> b) -> [a] -> [b]
def map[A, B] = (f: A => B) => (list: List[A]) => list.map(f)
```

__Further reading__
* [Ramda's type signatures](https://github.com/ramda/ramda/wiki/Type-Signatures)
* [Mostly Adequate Guide](https://drboolean.gitbooks.io/mostly-adequate-guide/content/ch7.html#whats-your-type)
* [What is Hindley-Milner?](http://stackoverflow.com/a/399392/22425) on Stack Overflow

## Algebraic data type
A composite type made from putting other types together. Two common classes of algebraic types are [sum](#sum-type) and [product](#product-type).

### Sum type
A Sum type is the combination of two types together into another one. It is called sum because the number of possible values in the result type is the sum of the input types.

we can use `sealed trait` or `Either` to have this type:
```tut:book
// imagine that rather than sets here we have types that can only have these values
sealed trait Bool
object True extends Bool
object False extends Bool

sealed trait HalfTrue
object HalfTrue extends HalfTrue

// The weakLogic type contains the sum of the values from bools and halfTrue
type WeakLogicType = Either[Bool, HalfTrue]
val weakLogicValues: Set[Either[HalfTrue, Bool]] = Set(Right(True), Right(False), Left(HalfTrue))
```

Sum types are sometimes called union types, discriminated unions, or tagged unions.

There's a [couple](https://github.com/paldepind/union-type) [libraries](https://github.com/puffnfresh/daggy) in JS which help with defining and using union types.

Flow includes [union types](https://flow.org/en/docs/types/unions/) and TypeScript has [Enums](https://www.typescriptlang.org/docs/handbook/enums.html) to serve the same role.

### Product type

A **product** type combines types together in a way you're probably more familiar with:

```tut:book
// point :: (Number, Number) -> {x: Number, y: Number}
case class Point(x: Int, y: Int)
val point = (x: Int, y: Int) => Point(x, y)
```
It's called a product because the total possible values of the data structure is the product of the different values. Many languages have a tuple type which is the simplest formulation of a product type.

See also [Set theory](https://en.wikipedia.org/wiki/Set_theory).

## Option
Option is a [sum type](#sum-type) with two cases often called `Some` and `None`.

Option is useful for composing functions that might not return a value.

```tut:book
// Naive definition

trait MyOption[+A] {
  def map[B](f: A => B): MyOption[B]
  def flatMap[B](f: A => MyOption[B]): MyOption[B]
}

case class MySome[A](a: A) extends MyOption[A] {
  def map[B](f: A => B): MyOption[B] = MySome(f(a))
  def flatMap[B](f: A => MyOption[B]): MyOption[B] = f(a)
}

case object MyNone extends MyOption[Nothing] {
  def map[B](f: Nothing => B): MyOption[B] = this
  def flatMap[B](f: Nothing => MyOption[B]) = this
}

// maybeProp :: (String, {a}) -> Option a
def maybeProp[A, B](key: A, obj: Map[A, B]): Option[B] = obj.get(key)
```
Use `flatMap` to sequence functions that return `Option`s
```tut:book

// getItem :: Cart -> Option CartItem
def getItem[A](cart: Map[String, Map[String, A]]): Option[Map[String, A]] = maybeProp("item", cart)

// getPrice :: Item -> Option Number
def getPrice[A](item: Map[String, A]): Option[A] = maybeProp("price", item)

// getNestedPrice :: cart -> Option a
def getNestedPrice[A](cart: Map[String, Map[String, A]]) = getItem(cart).flatMap(getPrice)

getNestedPrice(Map())
getNestedPrice(Map("item" -> Map("foo" -> 1)))
getNestedPrice(Map("item" -> Map("price" -> 9.99)))
```

`Option` is also known as `Maybe`. `Some` is sometimes called `Just`. `None` is sometimes called `Nothing`.

## Functional Programming Libraries in Scala

* [cats](https://typelevel.org/cats/)
* [scalaz](https://github.com/scalaz/scalaz)
* [shapeless](https://github.com/milessabin/shapeless)
* [Monocle](https://github.com/julien-truffaut/Monocle)
* [Spire](https://github.com/non/spire)
* [Many typelevel projects...](https://typelevel.github.io/projects/)

---

__P.S:__ This repo is successful due to the wonderful [contributions](https://github.com/hemanth/functional-programming-jargon/graphs/contributors)!
