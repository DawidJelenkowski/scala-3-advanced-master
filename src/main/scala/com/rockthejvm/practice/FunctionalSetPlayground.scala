package com.rockthejvm.practice

abstract class FSet[A] extends (A => Boolean) {

  def head: A
  def tail: FSet[A]
  // main api
  def contains(elem: A): Boolean
  def apply(elem: A): Boolean = contains(elem)

  // adding an element to the set
  infix def +(elem: A): FSet[A]
  infix def ++(anotherSet: FSet[A]): FSet[A]

  // "classics"
  def map[B](f: A => B): FSet[B]
  def flatMap[B](f: A => FSet[B]): FSet[B]
  def filter(predicate: A => Boolean): FSet[A]
  def foreach(f: A => Unit): Unit

  // methods
  // delete an element from the set
  infix def -(elem: A): FSet[A]
  // difference with another set
  infix def --(anotherSet: FSet[A]): FSet[A]
  // intersection with another set
  infix def &(anotherSet: FSet[A]): FSet[A]

  // negation == all the elements of type A EXCEPT the elements in this set
  def unary_! : FSet[A] = new PBSet(x => !contains(x))
}

// example { x in N | x % 2 == 0 }
// propety-based set
class PBSet[A](property: A => Boolean) extends FSet[A] {
  def head: A = politelyFail()
  def tail: FSet[A] = politelyFail()

  // main api
  def contains(elem: A): Boolean = property(elem)

  // adding an element to the set
  // The lambda function (x => property(x) || x == elem) checks if x satisfies either of the following conditions:
  // 1. property(x) returns true for x. This implies that x is already present in the set.
  // 2. x is equal to the elem being added. This means that elem is not already present in the set, so it adds elem to the set.
  infix def +(elem: A): FSet[A] = new PBSet[A](x => x == elem || property(x))
  // if either true, add to the set
  infix def ++(anotherSet: FSet[A]): FSet[A] = new PBSet[A](x => property(x) || anotherSet(x))

  // "classics"
  def map[B](f: A => B): FSet[B] = politelyFail()
  def flatMap[B](f: A => FSet[B]): FSet[B] = politelyFail()
  def filter(predicate: A => Boolean): FSet[A] = new PBSet(x => property(x) && predicate(x))
  def foreach(f: A => Unit): Unit = politelyFail()

  // methods
  // delete an element from the set
  infix def -(elem: A): FSet[A] = filter(x => x != elem)
  // difference with another set
  infix def --(anotherSet: FSet[A]): FSet[A] = filter(!anotherSet)
  // intersection with another set
  infix def &(anotherSet: FSet[A]): FSet[A] = filter(anotherSet)

  private def politelyFail() = throw new IllegalArgumentException("I don't know if this set is iterable...")
}

case class Empty[A]() extends FSet[A] { // PBSet(x => false)
  override def head: A = throw new NoSuchElementException
  override def tail: FSet[A] = throw new NoSuchElementException
  override def contains(elem: A) = false
  // after adding an element, the new set will be a Cons
  infix def +(elem: A): FSet[A] = Cons(elem, this)
  infix def ++(anotherSet: FSet[A]): FSet[A] = anotherSet

  def map[B](f: A => B): FSet[B] = Empty()
  def flatMap[B](f: A => FSet[B]): FSet[B] = Empty()
  def filter(predicate: A => Boolean): FSet[A] = this
  def foreach(f: A => Unit): Unit = () // unit value

  infix def -(elem: A): FSet[A] = this
  infix def --(anotherSet: FSet[A]): FSet[A] = this
  infix def &(anotherSet: FSet[A]): FSet[A] = this
}

case class Cons[A](head: A, tail: FSet[A]) extends FSet[A] {
  override def contains(elem: A) = elem == head || tail.contains(elem)
  // after adding an element, the new set will be a Cons
  infix def +(elem: A): FSet[A] = { // Cons(elem, this) // add to the head
    if contains(elem) then this
    else Cons(elem, this)
  }
  infix def ++(anotherSet: FSet[A]): FSet[A] = { // tail ++ anotherSet + head
    @scala.annotation.tailrec
    def concatTailrec(remainingList: FSet[A], accumulator: FSet[A]): FSet[A] = {
      if (remainingList == Empty()) accumulator
      else concatTailrec(remainingList.tail, accumulator + remainingList.head)
    }
    concatTailrec(this, anotherSet)
  }
  def map[B](f: A => B): FSet[B] = tail.map(f) + f(head)
  def flatMap[B](f: A => FSet[B]): FSet[B] = tail.flatMap(f) ++ f(head)
  def filter(predicate: A => Boolean): FSet[A] = {
    val filteredTail = tail.filter(predicate)
    if predicate(head) then filteredTail + head
    else filteredTail
  }
  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

  infix def -(elem: A): FSet[A] = {
    if head == elem then tail
    else tail - elem + head
  }

  infix def --(anotherSet: FSet[A]): FSet[A] = {
    // filter(x => !anotherSet(x))
    // only if "!" (negation) is implemented in the FSet
    filter(!anotherSet)
  }
  infix def &(anotherSet: FSet[A]): FSet[A] = { // intersection == filtering
    filter(anotherSet)
  }
}
object FSet {
  def apply[A](values: A*): FSet[A] = {
    @scala.annotation.tailrec
    def buildSet(valuesSeq: Seq[A], accumulator: FSet[A]): FSet[A] =
      if valuesSeq.isEmpty then accumulator
      else buildSet(valuesSeq.tail, accumulator + valuesSeq.head)

    buildSet(values, Empty())
  }
}
object FunctionalSetPlayground {

  def main(args: Array[String]): Unit = {
    // val first5 = FSet(1,2,3,4,5)
    // val someNumbers = FSet(4,5,6,7,8)
    // println(first5.contains(5)) // true
    // println(first5(6)) // invoke apply // false
    // println((first5 + 10).contains(10)) // true
    // println(first5.map(_ * 2).contains(10)) // true
    // println(first5.map(_ % 2).contains(1)) // true
    // println(first5.flatMap(x => FSet(x, x + 1)).contains(7)) // false

    // // val aSet = Set(1,2,3)
    // // val aList = (1 to 10).toList
    // // println(aList.filter(aSet))

    // println((first5 - 3).contains(3)) // false
    // println((first5 -- someNumbers).contains(4)) // false
    // println((first5 & someNumbers).contains(4)) // true

    val naturals = new PBSet[Int](_ => true)
    println(naturals.contains(100)) // true
    println(!naturals.contains(0)) // false
    println((!naturals + 1 + 2 + 3).contains(3)) // true
    println(!naturals.map(_ + 1)) // throw
  }
}
