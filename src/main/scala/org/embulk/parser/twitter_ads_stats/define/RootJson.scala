package org.embulk.parser.twitter_ads_stats.define

import org.embulk.parser.twitter_ads_stats.{MetricElementNames, MetricTimeSeries, Segment}
import spray.json.{DefaultJsonProtocol, DeserializationException, JsArray, JsString, JsValue, RootJsonReader}

class RootJson(metricElementNames: MetricElementNames) extends DefaultJsonProtocol {

  object MetricsReader extends RootJsonReader[Metrics] {

    private def readMetricTimeSeries(jsValue: JsValue): MetricTimeSeries = {
      jsValue match {
        case JsArray(arr) => Some(arr.map(_.convertTo[Long]))
        case _            => None
      }
    }

    override def read(json: JsValue): Metrics = {
      @scala.annotation.tailrec
      def loop(curMetricNames: List[String], curJsValue: Option[JsValue]): MetricTimeSeries = {
        curMetricNames match {
          case Nil =>
            curJsValue.flatMap(readMetricTimeSeries)
          case x :: xs =>
            loop(xs, curJsValue.flatMap(_.asJsObject.fields.get(x)))
        }
      }

      metricElementNames.resolveMetrics(loop, json.asJsObject)
    }
  }

  object IDDataReader extends RootJsonReader[IDData] {
    override def read(json: JsValue): IDData = {
      val fieldNames = IDData.fieldNames.toList
      json.asJsObject.getFields(fieldNames: _*) match {
        case Seq(a: JsValue, b) =>
          IDData(
            MetricsReader.read(a.asJsObject),
            b.convertTo[Option[Segment]]
          )
        case x => throw DeserializationException(msg = s"id_data can't deserialize json: $x", fieldNames = fieldNames)
      }
    }
  }

  object DataReader extends RootJsonReader[Data] {
    override def read(json: JsValue): Data = {
      val fieldNames = Data.fieldNames.toList
      json.asJsObject.getFields(fieldNames: _*) match {
        case Seq(a, JsArray(b)) =>
          Data(
            a.convertTo[String],
            b.map(IDDataReader.read)
          )
        case x => throw DeserializationException(msg = s"data can't deserialize json: $x", fieldNames = fieldNames)
      }
    }
  }

  object RequestFormat extends RootJsonReader[Request] {

    implicit object ParamsJsonFormat extends RootJsonReader[Params] {

      override def read(json: JsValue): Params = {
        val fieldNames = Params.fieldNames.toList
        json.asJsObject.getFields(fieldNames: _*) match {
          case Seq(JsString(a), JsString(b), c, segmentationType) =>
            Params(
              StatsDateTime(a),
              StatsDateTime(b),
              c.convertTo[String],
              segmentationType.convertTo[Option[String]]
            )
          case x => throw DeserializationException(msg = s"params can't deserialize json: $x", fieldNames = fieldNames)
        }
      }
    }

    override def read(json: JsValue): Request = {
      val fieldNames = Request.fieldNames.toList
      json.asJsObject.getFields(fieldNames: _*) match {
        case Seq(params) =>
          Request(ParamsJsonFormat.read(params))
        case x => throw DeserializationException(msg = s"request can't deserialize json: $x", fieldNames = fieldNames)
      }
    }
  }

  object RootReader extends RootJsonReader[Root] {
    override def read(json: JsValue): Root = {
      val fieldNames = Root.fieldNames.toList
      json.asJsObject.getFields(fieldNames: _*) match {
        case Seq(JsArray(data), request) =>
          Root(
            data.map(DataReader.read),
            RequestFormat.read(request)
          )
        case x => throw DeserializationException(msg = s"root can't deserialize json: $x", fieldNames = fieldNames)
      }
    }
  }

}
