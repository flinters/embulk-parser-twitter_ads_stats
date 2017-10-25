package org.embulk.parser.twitter_ads_stats.define

import scala.reflect.ClassTag

object FieldNameUtil {
  //Innerクラスは利用しないこと
  def fieldList[T](implicit tag: ClassTag[T]): Array[String] =
    tag.runtimeClass.getDeclaredFields.map(_.getName)
}
