package org.embulk.parser.twitter_ads_stats

import spray.json.DefaultJsonProtocol._
import spray.json._

object SegmentJson {
  implicit object SegmentJsonWriter extends RootJsonWriter[Segment] {

    private def toJValue(element: String): JsValue = element.toJson

    override def write(obj: Segment): JsValue = {
      val jsFields = obj.map(v => (v._1, toJValue(v._2)))
      JsObject(jsFields)
    }
  }
}
