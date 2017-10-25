package org.embulk.parser.twitter_ads_stats

import spray.json.DefaultJsonProtocol._
import spray.json._

object MetricsGroupJson {
  implicit object MetricsGroupJsonWriter extends RootJsonWriter[MetricsGroup] {

    private def toJValue(element: Option[Long]): JsValue = element match {
      case Some(e) => e.toJson
      case None => JsNull
    }

    override def write(obj: MetricsGroup): JsValue = {
      val jsFields = obj.map(v => (v._1, toJValue(v._2)))
      JsObject(jsFields)
    }
  }
}
