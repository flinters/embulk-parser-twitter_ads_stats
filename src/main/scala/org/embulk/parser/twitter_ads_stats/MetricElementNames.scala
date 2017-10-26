package org.embulk.parser.twitter_ads_stats

import org.embulk.parser.twitter_ads_stats.define.Metrics
import spray.json.{JsObject, JsValue}

/**
  * Metricの要素名
  *
  *
  * @param names key: MetricsGroup名 value: Metricの要素s
  * (example)
  * Map(
  *  "engagement" -> Seq("engagements", "impressions"),
  *  "web_conversion" -> Seq("conversion_purchases.assisted", "conversion_sign_ups.assisted")
  * )
  */
case class MetricElementNames(names: Map[String, Seq[String]]) {

  import MetricElementNames._

  def getSortedMetricsGroupNames: List[String] = names.keys.toList.sorted

  private[twitter_ads_stats] def resolveMetrics(
      resolveMetricTimeSeries: (List[String], Option[JsValue]) => MetricTimeSeries,
      json: JsObject
  ): Metrics =
    Metrics(
      names.flatMap { v =>
        v._2.map { value =>
          val keys = splitSeparator(value)
          (keys.mkString(separator), resolveMetricTimeSeries(keys, Some(json)))
        }
      }
    )
}

object MetricElementNames {

  private val separator = "_"

  private def splitSeparator(name: String): List[String] = {
    name.split("[.+]").toList
  }

  // @todo MetricElementNamesのスコープ内に閉じられるように
  // @todo 例外処理
  def replaceSeparator(name: String): String = {
    name.replaceAll("[.]", separator)
  }
}
