package com.rockthejvm.practice

abstract class FSet[A] extends (A => Boolean) {
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
}

case class Cons[A](head: A, tail: FSet[A]) extends FSet[A]
object FunctionalSetPlayground {

  def main(args: Array[String]): Unit = {

  }
}
