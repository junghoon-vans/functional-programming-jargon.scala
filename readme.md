# Scala code examples for Functional Programming Jargon

이 문서는 [ikhoon/functional-programming-jargon.scala](https://github.com/ikhoon/functional-programming-jargon.scala)의 번역본입니다.

# Functional Programming Jargon

함수형 프로그래밍(FP)은 많은 이점을 제공하며, 그 결과 그 인기가 높아지고 있습니다. 그러나 각 프로그래밍 패러다임에는 고유한 전문 용어가 존재하며 FP도 예외는 아닙니다. 용어집을 제공함으로써 FP를 더 쉽게 배울 수 있기를 바랍니다.

예제는 Scala로 제공됩니다. [Why Scala 3?](https://docs.scala-lang.org/scala3/book/why-scala-3.html#)

이것은 [WIP](https://github.com/hemanth/functional-programming-jargon/issues/20)입니다. 자유롭게 PR을 보내주세요. ;)

해당되는 경우, 본 문서에서는 [Fantasy Land spec](https://github.com/fantasyland/fantasy-land)에 정의된 용어를 사용합니다.

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

함수가 받는 인수의 개수입니다. 단항, 바이너리, 삼항 등과 같은 단어에서 유래했습니다. 이 단어는 두 개의 접미사, "-ary"와 "-ity"로 구성되어 있다는 특징이 있습니다. 예를 들어 덧셈은 두 개의 인수가 필요하므로 이진 함수 또는 항수(Arity)가 2인 함수로 정의됩니다. 이러한 함수는 라틴어보다 그리스어 어원을 선호하는 사람들에 의해 때때로 "dyadic"이라고 불릴 수 있습니다. 마찬가지로 가변적인 수의 인수를 받는 함수를 "가변적"이라고 하는 반면, 이진 함수는 커링(Currying) 및 부분 적용(Partial Application)에도 불구하고 두 개의 인수를 받아야 합니다. (아래 참조)

```scala
val sum = (a : Int, b: Int) => a + b // Arity는 2입니다.
// sum: (Int, Int) => Int = $$Lambda$6089/41702096@2b0a78f5
```

## Higher-Order Functions (HOF)

함수를 인수로 받거나 함수를 반환하는 함수입니다.

```scala
val filter = (predicate: Int => Boolean, xs: List[Int]) => xs.filter(predicate)
// filter: (Int => Boolean, List[Int]) => List[Int] = $$Lambda$6101/1664822486@6c2961d4
```
```scala
val isEven = (x: Int) => x % 2 == 0
// isEven: Int => Boolean = $$Lambda$6102/1380731259@7caf5594
```

```scala
filter(isEven, List(1, 2, 3, 4, 5))
// res0: List[Int] = List(2, 4)
```

## Partial Application

함수를 부분적으로 적용한다는 것은 원래 함수에 일부 인수를 미리 채워서 새 함수를 만드는 것을 의미합니다.

```scala
// 적용할 함수
val add3 = (a: Int, b: Int, c: Int) => a + b + c
// add3: (Int, Int, Int) => Int = $$Lambda$6104/749395786@26f51ccc

// `2`와 `3`을 `add3`에 부분적으로 적용하면 단일 인수 함수가 됩니다.
val fivePlus = add3(2, 3, _: Int) // (c) => 2 + 3 + c
// fivePlus: Int => Int = $$Lambda$6105/747846882@57c90a91

fivePlus(4)
// res3: Int = 9
```
부분 적용(Partial Application)은 데이터를 조작하여 복잡한 함수를 간단한 함수로 만드는 데 사용됩니다.
[Curried](#currying) 함수는 자동으로 부분 적용됩니다.

## Currying

여러 인수를 받는 함수를 한 번에 하나씩 받는 함수로 변환하는 프로세스입니다.

함수가 호출될 때마다 하나의 인수만 받고 모든 인수가 전달될 때까지 하나의 인수를 받는 함수를 반환합니다.

```scala
val sum = (a : Int, b: Int) => a + b
// sum: (Int, Int) => Int = $$Lambda$6106/477864989@f720751

val curriedSum = (a: Int) => (b: Int) => a + b
// curriedSum: Int => (Int => Int) = $$Lambda$6107/1895392220@447d87e6

curriedSum(40)(2) // 42.
// res4: Int = 42

val add2 = curriedSum(2) // (b) => 2 + b
// add2: Int => Int = $$Lambda$6108/1784411761@45922de3

add2(10) // 12
// res5: Int = 12
```

## Closure

클로저(Closure)는 범위 밖에서 변수에 액세스하는 방법입니다.
공식적으로 클로저는 어휘 범위가 지정된 네임드 바인딩을 구현하는 기술입니다. 환경과 함께 함수를 저장하는 방법입니다.

클로저는 실행이 함수가 정의된 블록을 벗어난 후에도 액세스를 위해 함수의 로컬 변수를 캡처하는 스코프입니다.
즉, 변수가 선언된 블록의 실행이 끝난 후에도 스코프를 참조할 수 있습니다.


```scala
val addTo = (x :Int) => (y: Int) => x + y
// addTo: Int => (Int => Int) = $$Lambda$6109/741101144@c019bed

val addToFive = addTo(5)
// addToFive: Int => Int = $$Lambda$6110/1430362686@30d35a2d

addToFive(3) // returns 8
// res6: Int = 8
```

함수 `addTo()`는 함수(내부적으로 `add()`라고 함)를 반환하고, 이를 매개변수 5를 가진 curried 호출을 통해 `addToFive`라는 변수에 저장합니다.

이상적으로는 `addTo` 함수가 실행을 완료하면 로컬 변수 add, x, y를 포함한 해당 범위에 접근할 수 없어야 합니다.하지만 `addToFive()`를 호출하면 8을 반환합니다. 즉, 코드 블록의 실행이 완료된 후에도 `addTo` 함수의 상태가 저장되며, 그렇지 않으면 `addTo`가 `addTo(5)`로 호출되었고 x의 값이 5로 설정되었다는 것을 알 방법이 없습니다.

어휘 범위 지정은 실행이 완료된 부모의 개인 변수인 x와 add의 값을 찾을 수 있는 이유입니다. 이 값을 클로저라고 합니다.

함수의 어휘 범위와 함께 스택은 부모에 대한 참조 형태로 저장됩니다. 이렇게 하면 클로저와 기본 변수가 가비지 수집되는 것을 방지할 수 있습니다(클로저에 대한 라이브 참조가 하나 이상 있기 때문입니다).

Lambda Vs Closure: 람다는 본질적으로 함수를 선언하는 표준 방식이 아닌 인라인으로 정의되는 함수입니다. 람다는 종종 객체로 전달될 수 있습니다.

클로저는 본문 외부의 필드를 참조하여 주변 상태를 둘러싸는 함수입니다. 닫힌 상태는 클로저를 호출할 때마다 유지됩니다.

__Further reading/Sources__
* [Lambda Vs Closure](http://stackoverflow.com/questions/220658/what-is-the-difference-between-a-closure-and-a-lambda)
* [How do JavaScript Closures Work?](http://stackoverflow.com/questions/111102/how-do-javascript-closures-work)

## Auto Currying

여러 인수를 받는 함수를 올바른 인수 수보다 적은 인수가 주어질 경우 나머지 인수를 받는 함수로 변환하면 함수가 반환됩니다. 함수가 올바른 수의 인수를 받으면 평가됩니다.

lodash와 Ramda에는 이런 방식으로 작동하는 'curry' 기능이 있습니다.

```scala
val add = (x: Int, y: Int) => x + y
// add: (Int, Int) => Int = $$Lambda$6111/885864900@6fc805b3

val curriedAdd = add.curried
// curriedAdd: Int => (Int => Int) = scala.Function2$$Lambda$3285/878350569@583d1436

curriedAdd(2) // (y) => 2 + y
// res7: Int => Int = scala.Function2$$Lambda$3286/1189535279@75c6a7a5

curriedAdd(1)(2) // 3
// res8: Int = 3
```

__Further reading__
* [Favoring Curry](http://fr.umio.us/favoring-curry/)
* [Hey Underscore, You're Doing It Wrong!](https://www.youtube.com/watch?v=m3svKOdZijA)

## Function Composition

두 함수를 결합하여 한 함수의 출력이 다른 함수의 입력이 되는 세 번째 함수를 형성하는 행위입니다.

```scala
def compose[A, B, C](f: B => C, g: A => B) = (a: A) => f(g(a)) // Definition
// compose: [A, B, C](f: B => C, g: A => B)A => C

val floorAndToString = compose((x: Double) => x.toString, math.floor) // Usage
// floorAndToString: Double => String = $$Lambda$6114/392323038@14236e09

floorAndToString(121.212121) // '121.0'
// res9: String = 121.0
```

## Continuation

프로그램의 특정 지점에서 아직 실행되지 않은 코드 부분을 연속(Continuation)이라고 합니다.

```scala
def printAsString(num: Int) = println(s"Given $num")
// printAsString: (num: Int)Unit

val printAsString= (num: Int) => println(s"Given $num")
// printAsString: Int => Unit = $$Lambda$6115/353404609@582dea2f

val addOneAndContinue = (num: Int, cc: Int => Any) => {
  val result = num + 1
  cc(result)
}
// addOneAndContinue: (Int, Int => Any) => Any = $$Lambda$6116/209832866@9488095

addOneAndContinue(2, printAsString) 
// Given 3
// res10: Any = ()
```

연속은 비동기 프로그래밍에서 프로그램이 계속 진행하기 전에 데이터를 수신할 때까지 기다려야 할 때 종종 볼 수 있습니다. 응답이 수신되면 프로그램의 나머지 부분, 즉 연속으로 전달되는 경우가 많습니다.

```scala
def continueProgramWith(data: String) = {
  // Continues program with data
}
// continueProgramWith: (data: String)Unit

def readFileAsync(file: String, cb: (Option[Throwable], String) => Unit) = {}
// readFileAsync: (file: String, cb: (Option[Throwable], String) => Unit)Unit

readFileAsync("path/to/file", (err, response) => {
  if (err.isDefined) {
    // handle error
    ()
  }
  continueProgramWith(response)
})
```

## Purity

함수는 반환값이 입력값에 의해서만 결정되고 부작용(Side Effects)을 생성하지 않으면 순수 함수(Pure Function)입니다.

```scala
val greet = (name: String) => s"Hi, ${name}"
// greet: String => String = $$Lambda$6118/976300008@d803871

greet("Brianne")
// res12: String = Hi, Brianne
```

다음 각 항목과 반대입니다:

```scala
var name = "Brianne"
// name: String = Brianne

def greet = () => s"Hi, ${name}"
// greet: () => String

greet() 
// res13: String = Hi, Brianne
```

위 예제의 출력은 함수 외부에 저장된 데이터를 기반으로 합니다...

```scala
var greeting: String = _
// greeting: String = null

val greet = (name: String) => {
  greeting = s"Hi, ${name}"
}
// greet: String => Unit = $$Lambda$6120/1120068379@6bf02320

greet("Brianne")

greeting 
// res15: String = Hi, Brianne
```

... 그리고 이것은 함수 외부에서 상태를 수정합니다.

## Side effects

함수나 표현식이 값을 반환하는 것 외에 외부의 변경 가능한 상태와 상호작용(읽기 또는 쓰기)하는 경우 부작용(Side Effects)이 있다고 합니다.

```scala
import java.util.Date
// import java.util.Date

def differentEveryTime = new Date()
// differentEveryTime: java.util.Date
```

```scala
println("IO is a side effect!")
// IO is a side effect!
```

## Idempotent

함수를 결과에 다시 적용해도 다른 결과가 나오지 않으면 함수는 멱등성(Idempotent)을 가집니다.

```
f(f(x)) ≍ f(x)
```

```scala
math.abs(math.abs(10))
// res17: Int = 10
```

```scala
def sort[A: Ordering](xs: List[A]) = xs.sorted
// sort: [A](xs: List[A])(implicit evidence$1: Ordering[A])List[A]

sort(sort(sort(List(2, 1))))
// res18: List[Int] = List(1, 2)
```

## Point-Free Style

정의에 사용된 인수가 명시적으로 식별되지 않는 함수를 작성합니다. 이 스타일은 보통 [currying](#currying) 또는 기타 [Higher-Order functions](#higher-order-functions-hof)가 필요합니다. 암묵적 프로그래밍이라고도 합니다.

```scala
// Given
def map[A, B](fn: A => B) = (list: List[A]) => list.map(fn)
// map: [A, B](fn: A => B)List[A] => List[B]

val add = (a: Int) => (b: Int) => a + b
// add: Int => (Int => Int) = $$Lambda$6121/1340871739@61a102c5

// Then

// Not points-free - `numbers` is an explicit argument
val incrementAll = (numbers: List[Int]) => map(add(1))(numbers)
// incrementAll: List[Int] => List[Int] = $$Lambda$6122/1403313951@7e5972fd

// Points-free - The list is an implicit argument
val incrementAll2 = map(add(1))
// incrementAll2: List[Int] => List[Int] = $$Lambda$6124/221818483@221baf45
```

`incrementAll`은 매개변수 `numbers`를 식별하고 사용하므로 포인트가 없습니다. `incrementAll2`는 인수를 언급하지 않고 함수와 값만 결합하여 작성됩니다. 이 함수는 포인트가 없습니다.

포인트가 없는 함수 정의는 `function`이나 `=>`가 없는 일반 대입과 똑같습니다.

## Predicate

술어(Predicate)는 주어진 값에 대해 참 또는 거짓을 반환하는 함수입니다. 술어는 일반적으로 배열 필터의 콜백으로 사용됩니다.

```scala
val predicate = (a: Int) => a > 2
// predicate: Int => Boolean = $$Lambda$6125/2085181758@1e842ab7

List(1, 2, 3, 4).filter(predicate)
// res24: List[Int] = List(3, 4)
```

## Contracts

컨트랙트(Contracts)는 런타임에 함수나 표현식의 동작에 대한 의무와 보증을 명시합니다. 이는 함수나 표현식의 입력과 출력에서 예상되는 일련의 규칙 역할을 하며, 일반적으로 계약이 위반될 때마다 오류가 보고됩니다.

```scala
scala> // Define our contract : int -> int
     | def contract[A](input: A): Boolean = input match {
     |   case _ : Int => true 
     |   case _ => throw new Exception("Contract violated: expected int -> int")
     | }
contract: [A](input: A)Boolean

scala> def addOne[A](num: A): Int = {
     |   contract(num)
     |   num.asInstanceOf[Int] + 1
     | }
addOne: [A](num: A)Int

scala> addOne(2) // 3
res26: Int = 3

scala> addOne("some string") // Contract violated: expected int -> int
java.lang.Exception: Contract violated: expected int -> int
  at .contract(<console>:16)
  at .addOne(<console>:15)
  ... 43 elided
```

## Category

범주(Category) 이론에서 범주는 객체와 객체 사이의 모피즘(Morphism)의 집합입니다. 프로그래밍에서는 일반적으로 타입은 객체 역할을 하고 함수는 형태소 역할을 합니다.

유효한 카테고리 3 규칙을 충족해야 합니다:

1. 객체를 그 자체에 매핑하는 아이덴티티 모피즘이 있어야 합니다.
    `a`가 어떤 범주의 객체인 경우,
    `a -> a`의 함수가 있어야 합니다.
2. 모피즘은 반드시 구성해야 합니다.
    `a`, `b`, `c`가 어떤 범주의 객체이고,
    `f`가 `a -> b`의 모피즘이고, `g`가 `b -> c`의 모피즘이면;
    `g(f(x))`는 `(g • f)(x)`와 동등해야 합니다.
    `g(f(x))` must be equivalent to `(g • f)(x)`.
3. 구성은 결합적이어야 합니다.
    `f • (g • h)`는 `(f • g) • h`와 같습니다.

이러한 규칙은 매우 추상적인 수준에서 구성을 지배하기 때문에 카테고리 이론은 새로운 구성 방법을 발견하는 데 탁월합니다.

__Further reading__

* [Category Theory for Programmers](https://bartoszmilewski.com/2014/10/28/category-theory-for-programmers-the-preface/)

## Value

변수에 할당할 수 있는 모든 항목입니다.

```scala
case class Person(name: String, age: Int)
// Person 클래스 정의

5
// res28: Int = 5

Person("John", 30) 
// res29: Person = Person(John,30)

(a: Any) => a
// res30: Any => Any = $$Lambda$6126/1828268232@117ca3b4

List(1)
// res31: List[Int] = List(1)

null
// res32: Null = null
```

## Constant

한번 정의되면 재할당할 수 없는 변수입니다.

```scala
val five = 5
// five: Int = 5

val john = Person("John", 30)
// john: Person = Person(John,30)
```

상수(Constant)는 [참조적으로 투명](#referential-transparency)합니다. 즉, 결과에 영향을 주지 않고 나타내는 값으로 대체할 수 있습니다.

위의 두 상수를 사용하면 다음 표현식은 항상 `true`를 반환합니다.

```scala
john.age + five == Person("John", 30).age + 5
// res33: Boolean = true
```

## Functor

객체의 각 값에 대해 실행하여 새 객체를 생성하는 동안 두 가지 규칙을 준수하는 `map` 함수를 구현하는 객체입니다:

__Preserves identity__
```
object.map(x => x) ≍ object
```

__Composable__

```
object.map(compose(f, g)) ≍ object.map(g).map(f)
```

(`f`, `g` are arbitrary functions)

자바스크립트에서 흔히 사용되는 함수는 두 가지 함수의 규칙을 따르는 `Array`입니다:

```scala
List(1, 2, 3).map(x => x) 
// res34: List[Int] = List(1, 2, 3)
```

그리고

```scala
val f = (x: Int) => x + 1
// f: Int => Int = $$Lambda$6128/977330834@2ef88643

val g = (x: Int) => x * 2
// g: Int => Int = $$Lambda$6129/739719665@1b4dfd52

List(1, 2, 3).map(x => f(g(x)))
// res35: List[Int] = List(3, 5, 7)

List(1, 2, 3).map(g).map(f)    
// res36: List[Int] = List(3, 5, 7)
```

## Pointed Functor

단일 값을 _어떤_ 값이라도 넣는 `순수` 함수를 가진 애플리케이션입니다.

[cats](https://typelevel.org/cats/) [`Applicative#pure`](https://github.com/typelevel/cats/blob/31874ce42b7e98bbb436a430c6a708f8e9db0e6d/core/src/main/scala/cats/Applicative.scala#L23)를 추가하여 배열을 포인트된 펑터로 만듭니다.

```scala
import cats._
// import cats._

import cats.implicits._
// import cats.implicits._

Applicative[List].pure(1)
// res37: List[Int] = List(1)
```

## Lift

리프팅은 값을 [펑터](#pointed-functor)와 같은 객체에 넣는 것을 말합니다.
함수를 [적용성 펑터](#applicative-functor)로 리프팅하면 해당 함수에 있는 값에 대해서도 작동하도록 만들 수 있습니다.

일부 구현에는 `lift` 또는 `liftA2`라는 함수가 있어 함수에서 함수를 더 쉽게 실행할 수 있습니다.

```scala
def liftA2[F[_]: Monad, A, B, C](f: A => B => C)(a: F[A], b: F[B]) = {
  a.map(f).ap(b)
}
// warning: there was one feature warning; for details, enable `:setting -feature' or `:replay -feature'
// liftA2: [F[_], A, B, C](f: A => (B => C))(a: F[A], b: F[B])(implicit evidence$1: cats.Monad[F])F[C]

val mult = (a: Int) => (b: Int) => a * b
// mult: Int => (Int => Int) = $$Lambda$6131/1684188711@6840cae6

val liftedMult = liftA2[List, Int, Int, Int](mult) _
// liftedMult: (List[Int], List[Int]) => List[Int] = $$Lambda$6132/1015569705@69b98e8

liftedMult(List(1, 2), List(3))
// res38: List[Int] = List(3, 6)

liftA2((a: Int) => (b: Int) => a + b)(List(1, 2), List(3, 4))
// res39: List[Int] = List(4, 5, 5, 6)
```

인자가 하나인 함수를 들어 올려 적용하면 `map`과 동일한 작업이 수행됩니다.

```scala
val increment = (x: Int) => x + 1
// increment: Int => Int = $$Lambda$6137/1176025030@60dd7083

Applicative[List].lift(increment)(List(2))
// res40: List[Int] = List(3)

List(2).map(increment)
// res41: List[Int] = List(3)
```

## Referential Transparency

프로그램의 동작을 변경하지 않고 해당 값으로 대체할 수 있는 표현식을 값을 변경하지 않고 대체할 수 있는 표현식을 참조 투명성(Referential Transparency)이라고 합니다.

함수 인사말이 있다고 가정해 봅시다:

```scala
val greet = () => "Hello World!"
// greet: () => String = $$Lambda$6139/1754785613@1d1c9e6a
```

`greet()`의 모든 호출은 `Hello World!`로 대체될 수 있으므로 greet은 참조 투명합니다.

##  Equational Reasoning

애플리케이션이 표현식으로 구성되어 있고 부작용이 없을 때, 시스템에 대한 진실은 그 부분으로부터 도출될 수 있습니다.

## Lambda

값처럼 취급할 수 있는 익명 함수입니다.

```scala
(_: Int) + 1
// res42: Int => Int = $$Lambda$6140/835760611@2344fbd6

(x: Int) => x + 1
// res43: Int => Int = $$Lambda$6141/605433877@12d4147c
```
람다(Lambda)는 종종 Higher-Order functions에 인수로 전달됩니다.

```scala
List(1, 2).map(_ + 1)
// res44: List[Int] = List(2, 3)
```

변수에 람다를 할당할 수 있습니다.

```scala
val add1 = (a: Int) => a + 1
// add1: Int => Int = $$Lambda$6143/1533951980@6546b471
```

## Lambda Calculus

함수를 사용하여 [계산의 범용 모델](https://en.wikipedia.org/wiki/Lambda_calculus)을 만드는 수학의 한 분야입니다.

## Lazy evaluation

지연 평가(Lazy evaluation)는 값이 필요할 때까지 표현식의 평가를 지연시키는 필요에 따른 호출 평가 메커니즘입니다. 함수형 언어에서는 명령의 순서가 중요한 명령형 언어에서는 일반적으로 사용할 수 없는 무한 목록과 같은 구조를 사용할 수 있습니다.

```scala
lazy val rand: Double = {
  println("generate random value...")
  math.random()
}
// rand: Double = <lazy>
```

```scala
rand // 각 실행은 임의의 값을 제공하며, 표현식은 필요에 따라 평가됩니다.
// generate random value...
// res45: Double = 0.7668030551457087
```

## Monoid

해당 객체를 같은 유형의 다른 객체와 "결합"하는 함수가 있는 객체입니다.

간단한 모노이드(Monoid) 중 하나는 숫자를 더하는 것입니다:

```scala
1 + 1 
// res46: Int = 2
```
이 경우 숫자는 객체이고 `+`는 함수입니다.

값과 결합해도 값이 변경되지 않는 '동일성' 값도 존재해야 합니다.

덧셈의 동일성 값은 `0`입니다.

```scala
1 + 0 
// res47: Int = 1
```

또한 작업 그룹화가 결과에 영향을 미치지 않아야 합니다(연관성):

```scala
1 + (2 + 3) == (1 + 2) + 3
// res48: Boolean = true
```

배열 연결도 모노이드를 형성합니다:

```scala
List(1, 2) ::: List(3, 4)
// res49: List[Int] = List(1, 2, 3, 4)
```

ID 값은 빈 배열 `[]`입니다.

```scala
List(1, 2) ::: List()
// res50: List[Int] = List(1, 2)
```

identity와 compose 함수가 제공되면 함수 자체가 모노이드를 형성합니다:

```scala
def identity[A](a: A): A = a
// identity: [A](a: A)A

def compose[A, B, C](f: B => C, g: A => B) = (a: A) => f(g(a)) // 정의
// compose: [A, B, C](f: B => C, g: A => B)A => C
```
'foo'는 하나의 인수를 받는 모든 함수입니다.

```
compose(foo, identity) ≍ compose(identity, foo) ≍ foo
```

## Monad

모나드(Monad)는 [`pure`](#pointed-functor) 및 `flatMap` 함수가 있는 객체입니다. `flatMap`은 중첩된 객체의 중첩을 해제한다는 점을 제외하면 [`map`](#functor)와 비슷합니다.

```scala
// Implementation
trait Monad[F[_]] {
  def pure[A](a: A): F[A]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  def map[A, B](fa: F[A])(f: A => B): F[B]
}
// warning: there was one feature warning; for details, enable `:setting -feature' or `:replay -feature'
// defined trait Monad
// warning: previously defined object Monad is not a companion to trait Monad.
// Companions must be defined together; you may wish to use :paste mode for this.

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
// warning: there was one feature warning; for details, enable `:setting -feature' or `:replay -feature'
// defined object Monad
// warning: previously defined trait Monad is not a companion to object Monad.
// Companions must be defined together; you may wish to use :paste mode for this.

import Monad._
// import Monad._

// Usage
Monad[List].flatMap(List("cat,dog", "fish,bird"))(a => a.split(",").toList)
// res53: List[String] = List(cat, dog, fish, bird)

// Contrast to map
Monad[List].map(List("cat,dog", "fish,bird"))(a => a.split(",").toList)
// res55: List[List[String]] = List(List(cat, dog), List(fish, bird))
```

`순수`는 다른 함수형 언어에서는 `return`이라고도 합니다.
`flatMap`은 다른 언어에서는 `bind`라고도 합니다.

## Comonad

`extract` 및 `coflatMap` 함수가 있는 객체입니다.

```scala
trait Comonad[F[_]] {
  def extract[A](x: F[A]): A
  def coflatMap[A, B](fa: F[A])(f: F[A] => B): F[B]
}
// warning: there was one feature warning; for details, enable `:setting -feature' or `:replay -feature'
// defined trait Comonad
// warning: previously defined object Comonad is not a companion to trait Comonad.
// Companions must be defined together; you may wish to use :paste mode for this.

type Id[X] = X
// defined type alias Id

def id[X](x: X): Id[X] = x
// id: [X](x: X)Id[X]

object Comonad {
  def apply[F[_]](implicit ev: Comonad[F]) = ev

  implicit val idInstance: Comonad[Id] = new Comonad[Id] {
    def extract[A](x: Id[A]): A = x
    def coflatMap[A, B](fa: Id[A])(f: Id[A] => B): Id[B] = {
      id(f(fa))
    }
  }
}
// warning: there was one feature warning; for details, enable `:setting -feature' or `:replay -feature'
// defined object Comonad
// warning: previously defined trait Comonad is not a companion to object Comonad.
// Companions must be defined together; you may wish to use :paste mode for this.
```

추출은 펑터에서 값을 가져옵니다.

```scala
import Comonad._
// import Comonad._

Comonad[Id].extract(id(1))
// res56: Id[Int] = 1
```

Extend는 Comonad에서 함수를 실행합니다. 함수는 Comonad와 동일한 유형을 반환해야 합니다.

```scala
Comonad[Id].coflatMap[Int, Int](id(1))(co => Comonad[Id].extract(co) + 1)
// res57: Id[Int] = 2
```

## Applicative Functor

적용성 펑터(Applicative Functor)는 `ap` 함수가 있는 객체입니다. `ap`는 객체의 함수를 같은 유형의 다른 객체에 있는 값에 적용합니다.

```scala
// Implementation
trait Applicative[F[_]] {
  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
}
// warning: there was one feature warning; for details, enable `:setting -feature' or `:replay -feature'
// defined trait Applicative
// warning: previously defined object Applicative is not a companion to trait Applicative.
// Companions must be defined together; you may wish to use :paste mode for this.

object Applicative {
  def apply[F[_]](implicit ev: Applicative[F]) = ev
  
  implicit val listInstance = new Applicative[List] {
    def ap[A, B](ff: List[A => B])(fa: List[A]): List[B] =
      ff.foldLeft(List[B]()) { (acc, f) => acc ::: fa.map(f) }
  }
}
// warning: there was one feature warning; for details, enable `:setting -feature' or `:replay -feature'
// defined object Applicative
// warning: previously defined trait Applicative is not a companion to object Applicative.
// Companions must be defined together; you may wish to use :paste mode for this.

import Applicative._
// import Applicative._

// Example usage
Applicative[List].ap(List((_: Int) + 1))(List(1))
// res60: List[Int] = List(2)
```

두 개의 객체가 있고 그 콘텐츠에 이진 함수를 적용하려는 경우 유용합니다.

```scala
// Arrays that you want to combine
val arg1 = List(1, 3)
// arg1: List[Int] = List(1, 3)

val arg2 = List(4, 5)
// arg2: List[Int] = List(4, 5)

// combining function - must be curried for this to work
val add = (x: Int) => (y: Int) => x + y
// add: Int => (Int => Int) = $$Lambda$6151/266874536@144646d8

val partiallyAppiedAdds = Applicative[List].ap(List(add))(arg1) // [(y) => 1 + y, (y) => 3 + y]
// partiallyAppiedAdds: List[Int => Int] = List($$Lambda$6152/226672465@6ff3ff0, $$Lambda$6152/226672465@677536aa)
```

이렇게 하면 `ap`를 호출하여 결과를 얻을 수 있는 함수 배열이 제공됩니다:

```scala
Applicative[List].ap(partiallyAppiedAdds)(arg2)
// res63: List[Int] = List(5, 6, 7, 8)
```

## Morphism

변환 함수입니다.

### Endomorphism

입력 유형이 출력과 동일한 함수입니다.

```scala
// uppercase :: String -> String
val uppercase = (str: String) => str.toUpperCase
// uppercase: String => String = $$Lambda$6153/1625244377@4f348849

// decrement :: Number -> Number
val decrement = (x: Int) => x - 1
// decrement: Int => Int = $$Lambda$6154/147034451@797635f9
```

### Isomorphism

본질적으로 구조적이며 데이터 손실이 없는 두 가지 유형의 객체 간의 변환 쌍입니다.

예를 들어 2D 좌표는 배열 `[2,3]` 또는 객체 `{x: 2, y: 3}`.

```scala
// Providing functions to convert in both directions makes them isomorphic.
case class Coords(x: Int, y: Int)
// defined class Coords

val pairToCoords = (pair: (Int, Int)) => Coords(pair._1, pair._2)
// pairToCoords: ((Int, Int)) => Coords = $$Lambda$6155/416347946@4d78afea

val coordsToPair = (coods: Coords) => (coods.x, coods.y)
// coordsToPair: Coords => (Int, Int) = $$Lambda$6156/905411695@26eed67a

coordsToPair(pairToCoords((1, 2)))
// res67: (Int, Int) = (1,2)

pairToCoords(coordsToPair(Coords(1, 2)))
// res68: Coords = Coords(1,2)
```



## Setoid

같은 유형의 다른 객체를 비교하는 데 사용할 수 있는 'equals' 함수가 있는 객체입니다.

배열을 Setoid로 만듭니다:

```scala
trait Eq[A] {
  def eqv(x: A, y: A): Boolean
}
// defined trait Eq

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
// defined object Eq
// warning: previously defined trait Eq is not a companion to object Eq.
// Companions must be defined together; you may wish to use :paste mode for this.

import Eq._
// import Eq._

Array(1, 2) == Array(1, 2)
// res69: Boolean = false

Array(1, 2).eqv(Array(1, 2))
// res70: Boolean = true

Array(1, 2).eqv(Array(0))
// res71: Boolean = false
```

## Semigroup

같은 유형의 다른 객체와 결합하는 `combine` 함수가 있는 객체입니다.

```scala
trait Semigroup[A] {
  def combine(x: A, y: A): A
}
// defined trait Semigroup

object Semigroup {
  def apply[A](implicit ev: Semigroup[A]) = ev

  implicit def listInstance[B]: Semigroup[List[B]] = new Semigroup[List[B]] {
    def combine(x: List[B], y: List[B]): List[B] = x ::: y
  }
  
  implicit class SemigroupOps[A](x: A) {
    def combine(y: A)(implicit ev: Semigroup[A]): A = ev.combine(x, y)
  }

}
// defined object Semigroup
// warning: previously defined trait Semigroup is not a companion to object Semigroup.
// Companions must be defined together; you may wish to use :paste mode for this.

import Semigroup._
// import Semigroup._

Semigroup[List[Int]].combine(List(1), List(2))
// res0: List[Int] = List(1, 2)
```

Semigroup은 연관성과 임의의 곱에 의해 닫혀야 합니다.
Semigroup의 모든 x, y, z에 대해 (x-y)-z = x-(y-z)가 됩니다.

```scala
List(1).combine(List(2)).combine(List(3)) 
// res1: List[Int] = List(1, 2, 3)

List(1).combine(List(2).combine(List(3)))
// res2: List[Int] = List(1, 2, 3)
```


## Foldable

해당 객체를 다른 유형으로 변환할 수 있는 `foldr/l` 함수가 있는 객체입니다.

```scala
trait Foldable[F[_]] {
  def foldLeft[A, B](fa: F[A], b: B)(f: (B, A) => B): B
  def foldRight[A, B](fa: F[A], b: B)(f: (A, B) => B): B
}
// warning: there was one feature warning; for details, enable `:setting -feature' or `:replay -feature'
// defined trait Foldable

object Foldable {
  def apply[F[_]](implicit ev: Foldable[F]) = ev

  implicit val listInstance = new Foldable[List]{
    def foldLeft[A, B](fa: List[A], b: B)(f: (B, A) => B): B = fa match {
      case x :: xs => foldLeft(xs, f(b, x))(f)
      case Nil => b 
    }

    def foldRight[A, B](fa: List[A], b: B)(f: (A, B) => B): B = fa match {
      case x :: xs => f(x, foldRight(xs, b)(f))
      case Nil => b
    }
  }
}
// defined object Foldable

import Foldable._
// import Foldable._

def sum[A](xs: List[A])(implicit N: Numeric[A]) : A =
  Foldable[List].foldLeft(xs, N.zero) {
    case (acc, x) => N.plus(acc, x)
  }
// sum: [A](xs: List[A])(implicit N: Numeric[A])A

sum(List(1, 2, 3))
// res3: Int = 6
```

## Lens

렌즈(Lens)는 다른 데이터 구조체에 대한 게터와 변하지 않는 세터를 짝을 이루는 구조(종종 객체 또는 함수)입니다.

```scala
import cats.Functor
// import cats.Functor

import monocle.PLens
// import monocle.PLens

import monocle.Lens
// import monocle.Lens

// Using [Monocle's lens](https://github.com/julien-truffaut/Monocle)

// S the source of a PLens
// T the modified source of a PLens
// A the target of a PLens
// B the modified target of a PLens
abstract class PLens[S, T, A, B] { 

  /** get the target of a PLens */
  def get(s: S): A

  /** set polymorphically the target of a PLens using a function */
  def set(b: B): S => T

  /** modify polymorphically the target of a PLens using Functor function */
  def modifyF[F[_]: Functor](f: A => F[B])(s: S): F[T]

  /** modify polymorphically the target of a PLens using a function */
  def modify(f: A => B): S => T
}
// defined class PLens
// warning: previously defined object PLens is not a companion to class PLens.
// Companions must be defined together; you may wish to use :paste mode for this.

object Lens {
  /** alias for [[PLens]] apply with a monomorphic set function */
  def apply[S, A](get: S => A)(set: A => S => S): Lens[S, A] =
    PLens(get)(set)
}
// defined object Lens

case class Person(name: String)
// defined class Person

val nameLens = Lens[Person, String](_.name)(str => p => p.copy(name = str))
// nameLens: monocle.Lens[Person,String] = monocle.PLens$$anon$8@5b2aa650
```

주어진 데이터 구조에 대해 가져오기와 설정의 쌍을 사용하면 몇 가지 주요 기능을 사용할 수 있습니다.

```scala
val person = Person("Gertrude Blanch")
// person: Person = Person(Gertrude Blanch)

// invoke the getter
// get :: Person => String
nameLens.get(person)
// res12: String = Gertrude Blanch

// invoke the setter
// set :: String => Person => Person
nameLens.set("Shafi Goldwasser")(person)
// res15: Person = Person(Shafi Goldwasser)

// run a function on the value in the structure
// modify :: (String => String) => Person => Person
nameLens.modify(_.toUpperCase)(person)
// res18: Person = Person(GERTRUDE BLANCH)
```

렌즈도 컴포지션할 수 있습니다. 이를 통해 깊게 중첩된 데이터에 대한 변경 불가능한 업데이트를 쉽게 수행할 수 있습니다.

```scala
// This lens focuses on the first item in a non-empty array

def firstLens[A] = Lens[List[A], A] {
  // get first item in array
  _.head
} { 
  // non-mutating setter for first item in array
  x => xs => x :: xs.tail
}
// firstLens: [A]=> monocle.Lens[List[A],A]

val people = List(Person("Gertrude Blanch"), Person("Shafi Goldwasser"))
// people: List[Person] = List(Person(Gertrude Blanch), Person(Shafi Goldwasser))

// Despite what you may assume, lenses compose left-to-right.
(firstLens composeLens nameLens).modify(_.toUpperCase)(people)
// res22: List[Person] = List(Person(GERTRUDE BLANCH), Person(Shafi Goldwasser))
```

Other implementations:
* [Quicklens](https://github.com/adamw/quicklens) - Modify deeply nested case class fields
* [Sauron](https://github.com/pathikrit/sauron) - Yet another Scala lens macro, Lightweight lens library in less than 50-lines of Scala
* [scalaz.Lens](http://eed3si9n.com/learning-scalaz/Lens.html)
                                                                                


## Type Signatures 

스칼라의 모든 함수는 인수의 유형과 반환값을 표시합니다.

```scala
// functionName :: firstArgType -> secondArgType -> returnType

// add :: Number -> Number -> Number
val add = (x: Int) => (y: Int) => x + y
// add: Int => (Int => Int) = $$Lambda$6168/450833074@b76efce

// increment :: Number -> Number
val increment = (x: Int) => x + 1
// increment: Int => Int = $$Lambda$6169/2134951565@23429781
```

함수가 다른 함수를 인수로 받아들이는 경우 괄호로 묶입니다.

```scala
// call :: (a -> b) -> a -> b
def call[A, B] = (f: A => B) => (x: A) => f(x)
// call: [A, B]=> (A => B) => (A => B)
```

문자 `a`, `b`, `c`, `d`는 인자가 어떤 유형이든 될 수 있음을 나타내기 위해 사용됩니다. 다음 버전의 `map`은 어떤 타입 `a`의 값을 다른 타입 `b`로 변환하는 함수, `a` 타입의 값 배열을 받아 `b` 타입의 값 배열을 반환합니다.

```scala
// map :: (a -> b) -> [a] -> [b]
def map[A, B] = (f: A => B) => (list: List[A]) => list.map(f)
// map: [A, B]=> (A => B) => (List[A] => List[B])
```

__Further reading__
* [Ramda's type signatures](https://github.com/ramda/ramda/wiki/Type-Signatures)
* [Mostly Adequate Guide](https://drboolean.gitbooks.io/mostly-adequate-guide/content/ch7.html#whats-your-type)
* [What is Hindley-Milner?](http://stackoverflow.com/a/399392/22425) on Stack Overflow

## Algebraic data type

다른 유형을 함께 묶어 만든 복합 유형입니다. 대수 유형의 두 가지 일반적인 클래스는 [합](#sum-type)과 [곱](#product-type)입니다.

### Sum type

합계 유형(Sum type)은 두 유형을 하나로 결합한 것입니다. 결과 유형의 가능한 값의 수는 입력 유형의 합이므로 합이라고 합니다.

우리는 이 유형을 가지기 위해 `sealed trait` 또는 `Either`를 사용할 수 있습니다:

```scala
// imagine that rather than sets here we have types that can only have these values
sealed trait Bool
// defined trait Bool

object True extends Bool
// defined object True

object False extends Bool
// defined object False

sealed trait HalfTrue
// defined trait HalfTrue

object HalfTrue extends HalfTrue
// defined object HalfTrue
// warning: previously defined trait HalfTrue is not a companion to object HalfTrue.
// Companions must be defined together; you may wish to use :paste mode for this.

// The weakLogic type contains the sum of the values from bools and halfTrue
type WeakLogicType = Either[Bool, HalfTrue]
// defined type alias WeakLogicType

val weakLogicValues: Set[Either[HalfTrue, Bool]] = Set(Right(True), Right(False), Left(HalfTrue))
// weakLogicValues: Set[Either[HalfTrue,Bool]] = Set(Right(True$@c8afbd6), Right(False$@78d0f039), Left(HalfTrue$@76907679))
```

합계 유형은 유니온 유형, 차별 유니온 또는 태그가 지정된 유니온이라고도 합니다.

There's a [couple](https://github.com/paldepind/union-type) [libraries](https://github.com/puffnfresh/daggy) in JS which help with defining and using union types.

Flow includes [union types](https://flow.org/en/docs/types/unions/) and TypeScript has [Enums](https://www.typescriptlang.org/docs/handbook/enums.html) to serve the same role.

### Product type

**곱** 유형(Product type)은 아마도 더 익숙한 방식으로 유형을 결합합니다:

```scala
// point :: (Number, Number) -> {x: Number, y: Number}
case class Point(x: Int, y: Int)
// defined class Point

val point = (x: Int, y: Int) => Point(x, y)
// point: (Int, Int) => Point = $$Lambda$6170/1963675584@55a1298b
```

데이터 구조에서 가능한 총 값은 서로 다른 값의 곱이기 때문에 곱이라고 합니다. 많은 언어에는 제품 유형의 가장 간단한 공식인 튜플 유형이 있습니다.

See also [Set theory](https://en.wikipedia.org/wiki/Set_theory).

## Option

옵션은 [합계 유형](#sum-type)으로, 흔히 'Some'와 'None'이라는 두 가지 경우가 있습니다.

옵션은 값을 반환하지 않을 수 있는 함수를 작성할 때 유용합니다.

```scala
// Naive definition

trait MyOption[+A] {
  def map[B](f: A => B): MyOption[B]
  def flatMap[B](f: A => MyOption[B]): MyOption[B]
}
// defined trait MyOption

case class MySome[A](a: A) extends MyOption[A] {
  def map[B](f: A => B): MyOption[B] = MySome(f(a))
  def flatMap[B](f: A => MyOption[B]): MyOption[B] = f(a)
}
// defined class MySome

case object MyNone extends MyOption[Nothing] {
  def map[B](f: Nothing => B): MyOption[B] = this
  def flatMap[B](f: Nothing => MyOption[B]) = this
}
// defined object MyNone

// maybeProp :: (String, {a}) -> Option a
def maybeProp[A, B](key: A, obj: Map[A, B]): Option[B] = obj.get(key)
// maybeProp: [A, B](key: A, obj: Map[A,B])Option[B]
```
Use `flatMap` to sequence functions that return `Option`s
```scala
// getItem :: Cart -> Option CartItem
def getItem[A](cart: Map[String, Map[String, A]]): Option[Map[String, A]] = maybeProp("item", cart)
// getItem: [A](cart: Map[String,Map[String,A]])Option[Map[String,A]]

// getPrice :: Item -> Option Number
def getPrice[A](item: Map[String, A]): Option[A] = maybeProp("price", item)
// getPrice: [A](item: Map[String,A])Option[A]

// getNestedPrice :: cart -> Option a
def getNestedPrice[A](cart: Map[String, Map[String, A]]) = getItem(cart).flatMap(getPrice)
// getNestedPrice: [A](cart: Map[String,Map[String,A]])Option[A]

getNestedPrice(Map())
// res38: Option[Nothing] = None

getNestedPrice(Map("item" -> Map("foo" -> 1)))
// res39: Option[Int] = None

getNestedPrice(Map("item" -> Map("price" -> 9.99)))
// res40: Option[Double] = Some(9.99)
```

`Option`은 `Maybe`로도 알려져 있습니다. `Some`은 때때로 `Just`라고도 합니다. `None`은 때때로 `Nothing`이라고도 합니다.

## Functional Programming Libraries in Scala

* [cats](https://typelevel.org/cats/)
* [scalaz](https://github.com/scalaz/scalaz)
* [shapeless](https://github.com/milessabin/shapeless)
* [Monocle](https://github.com/julien-truffaut/Monocle)
* [Spire](https://github.com/non/spire)
* [Many typelevel projects...](https://typelevel.github.io/projects/)

---

__P.S:__ This repo is successful due to the wonderful [contributions](https://github.com/hemanth/functional-programming-jargon/graphs/contributors)!
