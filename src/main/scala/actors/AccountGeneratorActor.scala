package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

class AccountGeneratorActor(userDatabaseRef: ActorRef) extends Actor with ActorLogging{

  override def receive: PartialFunction[Any, Unit] = {

    case userDetails: List[String] =>
      userDetails.head match {
        case str: String =>
          val userName = userDetails(2)
          userDatabaseRef forward(userName, userDetails)

        case _ => log.info("Accounts cannot be created, Undefined for such values")
          sender() ! "Invalid Information"
      }
    case _ => log.info("invalid list received")
      sender() ! "Invalid Information"
  }
}

object AccountGeneratorActor {
  def props(userDatabaseRef: ActorRef): Props = Props(classOf[AccountGeneratorActor], userDatabaseRef)//.withDispatcher("my-dispatcher")

}
