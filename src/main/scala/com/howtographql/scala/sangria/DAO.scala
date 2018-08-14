package com.howtographql.scala.sangria
import com.howtographql.scala.sangria.models.Link
import scala.concurrent.Future
import DBSchema._
import slick.jdbc.H2Profile.api._

class DAO(db: Database) {
  def allLinks = db.run(Links.result)

  def getLinks(ids: Seq[Int]) = db.run(
    Links.filter(_.id inSet ids).result
  )
}