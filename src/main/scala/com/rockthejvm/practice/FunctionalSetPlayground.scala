package com.rockthejvm.practice

import scala.annotation.tailrec

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
}

case class Empty[A]() extends FSet[A] {
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
}

case class Cons[A](head: A, tail: FSet[A]) extends FSet[A] {
  override def contains(elem: A) = elem == head || tail.contains(elem)
  // after adding an element, the new set will be a Cons
  infix def +(elem: A): FSet[A] = { // Cons(elem, this) // add to the head
    if contains(elem) then this
    else Cons(elem, this)
  }
  infix def ++(anotherSet: FSet[A]): FSet[A] = { // tail ++ anotherSet + head
    @tailrec
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
}

object FSet {
  def apply[A](values: A*): FSet[A] = {
    @tailrec
    def buildSet(valuesSeq: Seq[A], accumulator: FSet[A]): FSet[A] =
      if valuesSeq.isEmpty then accumulator
      else buildSet(valuesSeq.tail, accumulator + valuesSeq.head)

    buildSet(values, Empty())
  }
}
object FunctionalSetPlayground {

  def main(args: Array[String]): Unit = {
    val first5 = FSet(1,2,3,4,5)
    val second5 = FSet(4,5,6,7,8)
    println(first5.contains(5)) // true
    println(first5(6)) // invoke apply // false
    println((first5 + 10).contains(10)) // true
    println(first5.map(_ * 2).contains(10)) // true
    println(first5.map(_ % 2).contains(1)) // true
    println(first5.flatMap(x => FSet(x, x + 1)).contains(7)) // false
  }
}
