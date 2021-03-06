package spire.math

import spire.algebra._
import spire.std._

import scala.{specialized => spec}

/**
 * TODO
 * 3. LiteralOps? Literal conversions?
 * 4. Review operator symbols?
 * 5. Support for more operators?
 * 6. Start to worry about things like e.g. pow(BigInt, BigInt)
 */

trait Numeric[@spec(Int,Long,Float,Double) A] extends Ring[A]
with AdditiveAbGroup[A] with MultiplicativeAbGroup[A] with NRoot[A]
with ConvertableFrom[A] with ConvertableTo[A] with IsReal[A]

object Numeric {
  implicit final val ByteIsNumeric: Numeric[Byte] = new ByteIsNumeric
  implicit final val ShortIsNumeric: Numeric[Short] = new ShortIsNumeric
  implicit final val IntIsNumeric: Numeric[Int] = new IntIsNumeric
  implicit final val LongIsNumeric: Numeric[Long] = new LongIsNumeric
  implicit final val FloatIsNumeric: Numeric[Float] = new FloatIsNumeric
  implicit final val DoubleIsNumeric: Numeric[Double] = new DoubleIsNumeric
  implicit final val BigIntIsNumeric: Numeric[BigInt] = new BigIntIsNumeric
  implicit final val BigDecimalIsNumeric: Numeric[BigDecimal] = new BigDecimalIsNumeric
  implicit final val AlgebraicIsNumeric: Numeric[Algebraic] = new AlgebraicIsNumeric
  implicit final val RealIsNumeric: Numeric[Real] = new RealIsNumeric

  private val defaultApprox = ApproximationContext(Rational(1, 1000000000))

  implicit def RationalIsNumeric(implicit ctx: ApproximationContext[Rational] = defaultApprox): Numeric[Rational] =
    new RationalIsNumeric

  implicit def complexIsNumeric[A: Fractional: Trig: IsReal] = new ComplexIsNumeric

  @inline final def apply[A](implicit ev: Numeric[A]):Numeric[A] = ev
}

@SerialVersionUID(0L)
private[math] class ByteIsNumeric extends Numeric[Byte] with ByteIsEuclideanRing with ByteIsNRoot
with ConvertableFromByte with ConvertableToByte with ByteIsReal with Serializable {
  override def fromInt(n: Int): Byte = n.toByte
  override def fromDouble(n: Double): Byte = n.toByte
  override def toDouble(n: Byte): Double = n.toDouble
  def div(a:Byte, b:Byte): Byte = (a / b).toByte
}

@SerialVersionUID(0L)
private[math] class ShortIsNumeric extends Numeric[Short] with ShortIsEuclideanRing with ShortIsNRoot
with ConvertableFromShort with ConvertableToShort with ShortIsReal with Serializable {
  override def fromInt(n: Int): Short = n.toShort
  override def fromDouble(n: Double): Short = n.toShort
  override def toDouble(n: Short): Double = n.toDouble
  def div(a:Short, b:Short): Short = (a / b).toShort
}

@SerialVersionUID(0L)
private[math] class IntIsNumeric extends Numeric[Int] with IntIsEuclideanRing with IntIsNRoot
with ConvertableFromInt with ConvertableToInt with IntIsReal with Serializable {
  override def fromInt(n: Int): Int = n
  override def fromDouble(n: Double): Int = n.toInt
  override def toDouble(n: Int): Double = n.toDouble
  def div(a: Int, b: Int): Int = a / b
}

@SerialVersionUID(0L)
private[math] class LongIsNumeric extends Numeric[Long] with LongIsEuclideanRing with LongIsNRoot
with ConvertableFromLong with ConvertableToLong with LongIsReal with Serializable {
  override def fromInt(n: Int): Long = n
  override def fromDouble(n: Double): Long = n.toLong
  override def toDouble(n: Long): Double = n.toDouble
  def div(a: Long, b: Long): Long = a / b
}

@SerialVersionUID(0L)
private[math] class BigIntIsNumeric extends Numeric[BigInt] with BigIntIsEuclideanRing
with BigIntIsNRoot with ConvertableFromBigInt with ConvertableToBigInt
with BigIntIsReal with Serializable {
  override def fromInt(n: Int): BigInt = BigInt(n)
  override def fromDouble(n: Double): BigInt = BigDecimal(n).toBigInt
  override def toDouble(n: BigInt): Double = n.toDouble
  def div(a: BigInt, b: BigInt): BigInt = a / b
}

@SerialVersionUID(0L)
private[math] class FloatIsNumeric extends Numeric[Float] with FloatIsField
with FloatIsNRoot with ConvertableFromFloat with ConvertableToFloat
with FloatIsReal with Serializable {
  override def fromInt(n: Int): Float = n.toFloat
  override def fromDouble(n: Double): Float = n.toFloat
  override def toDouble(n: Float): Double = n.toDouble
}

@SerialVersionUID(0L)
private[math] class DoubleIsNumeric extends Numeric[Double] with DoubleIsField
with DoubleIsNRoot with ConvertableFromDouble with ConvertableToDouble
with DoubleIsReal with Serializable {
  override def fromInt(n: Int): Double = n.toDouble
  override def fromDouble(n: Double): Double = n
  override def toDouble(n: Double): Double = n.toDouble
}

@SerialVersionUID(0L)
private[math] class BigDecimalIsNumeric extends Numeric[BigDecimal] with BigDecimalIsField
with BigDecimalIsNRoot with ConvertableFromBigDecimal with ConvertableToBigDecimal
with BigDecimalIsReal with Serializable {
  override def fromInt(n: Int): BigDecimal = BigDecimal(n)
  override def fromDouble(n: Double): BigDecimal = BigDecimal(n)
  override def toDouble(n: BigDecimal): Double = n.toDouble
}

@SerialVersionUID(0L)
private[math] class RationalIsNumeric(implicit val context: ApproximationContext[Rational])
extends Numeric[Rational] with RationalIsField with RationalIsNRoot
with ConvertableFromRational with ConvertableToRational
with RationalIsReal with Serializable {
  override def toDouble(n: Rational): Double = n.toDouble
  override def fromInt(n: Int): Rational = Rational(n)
  override def fromDouble(n: Double): Rational = Rational(n)
}

@SerialVersionUID(0L)
private[math] class AlgebraicIsNumeric extends Numeric[Algebraic] with AlgebraicIsField with AlgebraicIsNRoot
with ConvertableFromAlgebraic with ConvertableToAlgebraic with AlgebraicIsReal with Serializable {
  override def fromInt(n: Int): Algebraic = Algebraic(n)
  override def fromDouble(n: Double): Algebraic = Algebraic(n)
  override def toDouble(n: Algebraic): Double = n.toDouble
}

@SerialVersionUID(0L)
private[math] class RealIsNumeric extends Numeric[Real] with RealIsFractional with Serializable {
  override def fromInt(n: Int): Real = Real(n)
  override def fromDouble(n: Double): Real = Real(n)
  override def toDouble(n: Real): Double = n.toDouble
}

@SerialVersionUID(0L)
class ComplexIsNumeric[A](implicit
    val algebra: Fractional[A], val trig: Trig[A], val order: IsReal[A])
extends ComplexEq[A] with ComplexIsField[A] with Numeric[Complex[A]]
with ComplexIsTrig[A] with ComplexIsNRoot[A]
with ConvertableFromComplex[A] with ConvertableToComplex[A]
with Order[Complex[A]] with ComplexIsSigned[A] with Serializable {
  def nroot: NRoot[A] = algebra

  override def fromInt(n: Int): Complex[A] = Complex.fromInt[A](n)
  override def fromDouble(n: Double): Complex[A] = Complex[A](algebra.fromDouble(n))

  override def eqv(x: Complex[A], y: Complex[A]): Boolean = x == y
  override def nroot(a: Complex[A], n: Int) = a.pow(reciprocal(fromInt(n)))

  def compare(x:Complex[A], y:Complex[A]): Int =
    if (x eqv y) 0 else throw new UnsupportedOperationException("undefined")

  def ceil(a: Complex[A]): Complex[A] = a.ceil
  def floor(a: Complex[A]): Complex[A] = a.floor
  def isWhole(a: Complex[A]): Boolean = a.isWhole
  def round(a: Complex[A]): Complex[A] = a.round
}
