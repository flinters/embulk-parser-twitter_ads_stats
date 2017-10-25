package org.embulk.parser.twitter_ads_stats.define

import org.embulk.parser.twitter_ads_stats.{Column, MetricElementNames, ParseException}

case class Data(id: String, id_data: Seq[IDData]) {
  private[define] def resolveColumns(metricElementNames: MetricElementNames, request: Request): Either[ParseException, Seq[Column]] = {
    id_data.map { idData =>
      idData.resolveColumns(id, metricElementNames, request)
    }.foldRight[Either[ParseException, Seq[Column]]](Right(Nil)) {
      case (Left(e), _) => Left(e)
      case (Right(_), Left(e)) => Left(e)
      case (Right(seq1), Right(seq2)) => Right(seq1 ++: seq2)
    }
  }
}

object Data {
  val fieldNames:Array[String] = FieldNameUtil.fieldList[Data]
}