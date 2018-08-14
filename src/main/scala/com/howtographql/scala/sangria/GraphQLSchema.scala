package com.howtographql.scala.sangria

import sangria.schema.{ListType, ObjectType}
import models._
import sangria.execution.deferred.{DeferredResolver, Fetcher, HasId}
import sangria.schema._
import sangria.macros.derive._


object GraphQLSchema {

  implicit val LinkType = deriveObjectType[Unit, Link]()
  implicit val linkHasId = HasId[Link, Int](_.id)

  implicit val UserType = deriveObjectType[Unit, User]()
  implicit val userHasId = HasId[User, Int](_.id)

  val linksFetcher = Fetcher(
    (ctx: MyContext, ids: Seq[Int]) => ctx.dao.getLinks(ids)
  )

  val userFetcher = Fetcher (
    (ctx: MyContext, ids: Seq[Int]) => ctx.dao.getUsers(ids)
  )

  //this is a guess that multiple fetchers can be put in here. Need to check
  val Resolver = DeferredResolver.fetchers(linksFetcher, userFetcher)


  val Id = Argument("id", IntType)
  val Ids = Argument("ids", ListInputType(IntType))


  val QueryType = ObjectType(
    "Query",
    fields[MyContext, Unit](

      Field("allLinks", ListType(LinkType), resolve = c => c.ctx.dao.allLinks),
      Field("allUsers", ListType(UserType), resolve = c => c.ctx.dao.allUsers),

      Field("link",
        OptionType(LinkType),
        arguments = Id :: Nil,
        resolve = c => linksFetcher.deferOpt(c.arg(Id))
      ),

      Field("user",
        OptionType(UserType),
        arguments = Id :: Nil,
        resolve = c => userFetcher.deferOpt(c.arg(Id))
      ),

      Field("links",
        ListType(LinkType),
        arguments = Ids :: Nil,
        resolve = c => linksFetcher.deferSeq(c.arg(Ids))
      ),

      Field("users",
        ListType(UserType),
        arguments = Ids :: Nil,
        resolve = c => userFetcher.deferSeq(c.arg(Ids))
      )
    )
  )

  val SchemaDefinition = Schema(QueryType)
}