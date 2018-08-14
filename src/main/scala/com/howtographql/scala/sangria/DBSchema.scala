package com.howtographql.scala.sangria

import slick.jdbc.H2Profile.api._

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.language.postfixOps
import com.howtographql.scala.sangria.models._


object DBSchema {

  class UsersTable(tag: Tag) extends Table[User](tag, "USERS"){

    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def email = column[String]("EMAIL")
    def password = column[String]("PASSWORD")

    def * = (id, name, email, password).mapTo[User]

  }

  class LinksTable(tag: Tag) extends Table[Link](tag, "LINKS"){

    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def url = column[String]("URL")
    def description = column[String]("DESCRIPTION")

    def * = (id, url, description).mapTo[Link]

  }

  //2
  val Links = TableQuery[LinksTable]

  val Users = TableQuery[UsersTable]

  //3
  val databaseSetup = DBIO.seq(
    Links.schema.create,
    Users.schema.create,

    Links forceInsertAll Seq(
      Link(1, "http://howtographql.com", "This is a test of changing community driven GraphQL tutorial"),
      Link(2, "http://graphql.org", "Official GraphQL web page"),
      Link(3, "https://facebook.github.io/graphql/", "GraphQL specification")
    ),

    Users forceInsertAll Seq(
      User(1, "Mickelvy", "Mv@M.com", "cat"),
      User(2, "JohnJohn", "JJ@J.com", "dog"),
      User(3, "Testeruso", "T@T.com", "password")
    )
  )

  def createDatabase: DAO = {
    val db = Database.forConfig("h2mem")

    Await.result(db.run(databaseSetup), 10 seconds)

    new DAO(db)

  }

}
