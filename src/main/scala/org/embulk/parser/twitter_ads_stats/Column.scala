package org.embulk.parser.twitter_ads_stats

import java.time.LocalDate

import org.embulk.spi.`type`.Types
import org.embulk.spi.{Column => EmbulkColumn}

object Column {
  def createEmbulkColumns(metricElementNames: MetricElementNames): Seq[EmbulkColumn] = {
    @scala.annotation.tailrec
    def loop(
              curIndex: Int,
              curNames: List[String],
              acc: Seq[EmbulkColumn]
            ): Seq[EmbulkColumn] = {
      curNames match {
        case Nil => acc.reverse
        case x :: xs =>
          loop(
            curIndex + 1,
            xs,
            new EmbulkColumn(curIndex, x, Types.JSON) +: acc
          )
      }
    }


    val baseColumns = Seq(
      new EmbulkColumn(0, "id", Types.STRING),
      new EmbulkColumn(1, "date", Types.STRING),
      new EmbulkColumn(2, "segment", Types.STRING),
      new EmbulkColumn(3, "placement", Types.STRING)
    )

    val metricsColumns = loop(
      4,
      metricElementNames.getSortedMetricsGroupNames,
      Nil
    )
    baseColumns ++ metricsColumns
  }
}

case class Column(
                   id: String,
                   date: LocalDate,
                   segment: Option[String],
                   placement: String,
                   metricsGroup: Map[String, MetricsGroup]
                 )