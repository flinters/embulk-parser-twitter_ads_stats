package org.embulk.parser.twitter_ads_stats

import org.embulk.config._
import org.embulk.spi._
import org.embulk.spi.json.JsonParser
import org.embulk.spi.util.FileInputInputStream
import org.slf4j.Logger
import spray.json.{pimpAny, pimpString}

import scala.util.control.NonFatal
import scala.collection.JavaConverters._
import MetricsGroupJson._
import org.embulk.parser.twitter_ads_stats.define.{Root, RootJson}

class TwitterAdsStatsParserPlugin extends ParserPlugin {

  import TwitterAdsStatsParserPlugin._

  override def transaction(config: ConfigSource, control: ParserPlugin.Control): Unit = {
    val task = config.loadConfig(classOf[PluginTask])
    control.run(
      task.dump(),
      new Schema(Column.createEmbulkColumns(metricElementNames).asJava)
    )
  }

  override def run(taskSource: TaskSource, schema: Schema, input: FileInput, output: PageOutput): Unit = {

    val task = taskSource.loadTask(classOf[PluginTask])
    val stopOnInvalidRecord = task.getStopOnInvalidRecord

    LoanPattern(new PageBuilder(Exec.getBufferAllocator, schema, output)) { pb =>
      while (input.nextFile()) {
        (for {
          root <- createRootFrom(input)
          columns <- root.resolveColumns(metricElementNames)
        } yield addRecord(pb, columns, root)) match {
          case Right(_) =>
          case Left(e) =>
            if (stopOnInvalidRecord) {
              throw new DataException(e.getMessage, e)
            } else {
              logger.warn(s"Skipped invalid record $e")
            }
        }
      }
      pb.finish()
    }
  }

  private def addRecord(pb: PageBuilder, columns: Seq[Column], root: Root): Unit = {
    columns.foreach { column =>
      Column.createEmbulkColumns(metricElementNames).foreach { embulkColumn =>
        (column, embulkColumn.getName) match {
          case (Column(id, _, _, _, _), "id") =>
            pb.setString(embulkColumn, id)
          case (Column(_, date, _, _,_), "date") =>
            pb.setString(embulkColumn, date.toString)
          case (Column(_, _, Some(segment),_, _), "segment") =>
            pb.setString(embulkColumn, segment)
          case (Column(_, _, None,_, _), "segment") =>
            pb.setNull(embulkColumn)
          case (Column(_, _, _,placement, _), "placement") =>
            pb.setString(embulkColumn, placement)
          case (Column(_, _, _, _,metricsGroup), key) =>
            metricsGroup.get(key) match {
              case Some(m) =>
                pb.setJson(
                  embulkColumn,
                  jsonParser.parse(m.toJson.compactPrint)
                )
              case None =>
                pb.setNull(embulkColumn)

            }
          case _ => throw new RuntimeException
        }
      }
      pb.addRecord()
    }
  }

  private def createRootFrom(input: FileInput): Either[ParseException, Root] = {
    val stream = new FileInputInputStream(input)
    try {
      val jsValue = scala.io.Source.fromInputStream(stream).mkString.parseJson
      val root = new RootJson(metricElementNames).RootReader.read(jsValue)
      Right(root)
    } catch {
      case NonFatal(e) => Left(new InvalidInputFileException(e))
    }
  }
}

object TwitterAdsStatsParserPlugin {
  val logger: Logger = Exec.getLogger(classOf[TwitterAdsStatsParserPlugin])
  val jsonParser = new JsonParser
}
