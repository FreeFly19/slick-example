import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration._


object AsyncHelper {
  def exec[T](action: DBIO[T])(implicit db: Database): T = Await.result(db.run(action), 5.seconds)
}

import AsyncHelper._

object Example1 extends App {
  implicit val db = Database.forConfig("h2mem")

  val createTable = sql"CREATE TABLE USERS(ID BIGINT AUTO_INCREMENT, LOGIN VARCHAR(255), PRIMARY KEY(ID))".asUpdate
  def addUser(login: String) = sqlu"INSERT INTO USERS VALUES (null, ${login})"
  val users = sql"SELECT ID, LOGIN FROM USERS".as[(Long, String)]


  exec(createTable)
  exec(addUser("User 1"))
  exec(addUser("User 2"))
  exec(users).foreach(println)
}


object Example2 extends App {
  implicit val db = Database.forConfig("h2mem")

  case class User(id: Option[Long] = None, login: String) {
    def this(login: String) = this(None, login)
  }

  class Users(tag: Tag) extends Table[User](tag, "USERS") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def login = column[String]("LOGIN")

    def * = (id.?, login) <> (User.tupled, User.unapply)
  }

  val users = TableQuery[Users]

  exec(users.schema.create)

  exec(users += User(None, "User 1"))
  exec(users += User(login = "User 2"))
  exec(users += new User("User 3"))

  exec(users.result).foreach(println)
}
