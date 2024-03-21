package com.rockthejvm.practice

/* Goal
Write a lazily evaluated, potentially INFINITE linked list 
*/
abstract class LzList[A] {
  def isEmpty: Boolean
  def head: A
  def tail: LzList[A]

  // utilities
  def #::(element: A): LzList[A] // prepending
  infix def ++(another: => LzList[A]): LzList[A]

  // classics
  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): LzList[B]
  def flatMap[B](f: A => LzList[B]): LzList[B]
  def filter(predicate: A => Boolean): LzList[A]
  def withFilter(predicate: A => Boolean): LzList[A] = filter(predicate)

  def take(n: Int): LzList[A] // takes the first n elements from this lazy list
  def takeAsList(n: Int): List[A] = {
    take(n).toList
  }

  // use this carefully on large lazy lists
  def toList: List[A] = {
    @scala.annotation.tailrec
    def toListAux(remaining: LzList[A], acc: List[A]): List[A] = {
      if (remaining.isEmpty) acc.reverse
      else toListAux(remaining.tail, remaining.head :: acc)
    }
    toListAux(this, List())
  }
}

case class LzEmpty[A]() extends LzList[A] {
  def isEmpty: Boolean = true
  def head: A = throw new NoSuchElementException
  def tail: LzList[A] = throw new NoSuchElementException

  // utilities
  def #::(element: A): LzList[A] = new LzCons(element, this)
  infix def ++(another: => LzList[A]): LzList[A] = another

  // classics
  def foreach(f: A => Unit): Unit = ()
  def map[B](f: A => B): LzList[B] = LzEmpty()
  def flatMap[B](f: A => LzList[B]): LzList[B] = LzEmpty()
  def filter(predicate: A => Boolean): LzList[A] = this // there is nothing to filter

  def take(n: Int): LzList[A] = {
    if (n == 0) this
    else throw RuntimeException("Cannot take $n elements from an empty lazy list.")
  }
}

class LzCons[A](hd: => A, tl: => LzList[A]) extends LzList[A] {
  def isEmpty: Boolean = false
  override lazy val head: A = hd
  override lazy val tail: LzList[A] = tl

  // utilities
  def #::(element: A): LzList[A] = new LzCons(element, this)
  infix def ++(another: => LzList[A]): LzList[A] = new LzCons(head, tail ++ another)

  // classics
  def foreach(f: A => Unit): Unit = {
    @scala.annotation.tailrec
    def foreachTailrec(lzlist: LzList[A]): Unit = {
      if (lzlist.isEmpty) ()
      else {
        f(lzlist.head)
        foreachTailrec(lzlist.tail)
      }
    }
    foreachTailrec(this)
  }

  def map[B](f: A => B): LzList[B] = new LzCons(f(head), tail.map(f))
  def flatMap[B](f: A => LzList[B]): LzList[B] = f(head) ++ tail.flatMap(f) // preserves lazy evaluation
  def filter(predicate: A => Boolean): LzList[A] =
    if (predicate(head)) new LzCons(head, tail.filter(predicate)) // preserves lazy evaluation
    else tail.filter(predicate)

  def take(n: Int): LzList[A] = {
    if (n <= 0) LzEmpty()
    else if (n == 1) new LzCons(head, LzEmpty())
    else new LzCons(head, tail.take(n - 1)) // preserves lazy evaluation
  }
}

object LzList {
  // it was implemented so that compiler wouldn't be confused
  def empty[A]: LzList[A] = LzEmpty()

  def generate[A](start: A)(generator: A => A): LzList[A] =
    // this is recursive called; look at the main
    new LzCons(start, LzList.generate(generator(start))(generator))

  def from[A](list: List[A]): LzList[A] = list.reverse.foldLeft(LzList.empty) { 
    (currentLzList, newElement) => new LzCons(newElement, currentLzList)
  }
  
  def apply[A](values: A*): LzList[A] = from(values.toList)

  def fibonacci: LzList[BigInt] = {
    def fibo(first: BigInt, second: BigInt): LzList[BigInt] =
      new LzCons[BigInt](first, fibo(second, first + second))
    
    fibo(1,2)
  }
  
  /* 
    [2,3,4,5,6,7,8,9,10,11,12,13,14,15,...]
    [2,3,5,7,9,11,13,15,17,...]
    [2,3,5,7,11,13,17,19,23,25,29,...]
    [2,3,5,7,11,13,17,19,23,29,...]

    sieve([2,3,4,5,6,...]) =
    2 #:: sieve([3,4,5,6,...].filter(_ % 2 != = 0))
    2 #:: sieve([3,5,7,9,...])
    2 #:: 3 #:: sieve([5,7,8,11,...].filter(_ % 3 != 0))
    ... ad infinitium.
  */
  def eratosthenes: LzList[Int] = {

    def isPrime(n: Int) = {
      def isPrimeTailrec(potentialDivisor: Int): Boolean =
        if (potentialDivisor < 2) true
        else if (n % potentialDivisor == 0) false
        else isPrimeTailrec(potentialDivisor - 1)
      
      isPrimeTailrec(n / 2)
    }

    def sieve(numbers: LzList[Int]): LzList[Int] = {
      if (numbers.isEmpty) numbers
      else if (!isPrime(numbers.head)) sieve(numbers.tail) // if numbers.head is not a prime it skips it
      else new LzCons[Int](numbers.head, sieve(numbers.tail.filter(_ % numbers.head != 0)))
    }
    val naturalsFrom2 = LzList.generate(2)(_ + 1)
    sieve(naturalsFrom2)
  }
}

object LzListPlayground {
  def main(args: Array[String]): Unit = {
    val naturals = LzList.generate(1)(n => n + 1) // INFINITE list of natural numbers
    // println(naturals.head) // 1
    // println(naturals.tail.head) // 2
    // println(naturals.tail.tail.head) // 3

    val first50k = naturals.take(50000)
    // first50k.foreach(println)
    val first50kList = first50k.toList
    // println(first50kList)
  
    // println(naturals.map(_ * 2).takeAsList(100))

    // without apply method
    // println(naturals.flatMap(x => LzCons(x, new LzCons(x + 1, LzEmpty()))))
    // println(naturals.flatMap(x => LzList(x, x + 1)).takeAsList(100))

    // println(naturals.filter(_ < 10).takeAsList(9))
    // println(naturals.filter(_ < 10).takeAsList(10)) // crash with stack overflow or infinite recursion

    val combinationsLazy: LzList[String] = for {
      number <- LzList(1,2,3)
      string <- LzList("black", "white")
    } yield s"$number-$string"
    // println(combinationsLazy.toList)
  
  /**
    * Exercise:
      1. Lazy list of Fibonacci numbers
      2. Infinite list of prime numbers
      - filter with isPrime
      - Eratosthenes' sieve

    */

    val fibos = LzList.fibonacci
    // println(fibos.takeAsList(100))

    val primes = LzList.eratosthenes
    println(primes.takeAsList(100))
  }
}