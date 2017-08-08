package models

case class UserAccount(accountNumber: Long, accountHolderName: String, address: String, userName: String, initialAmount: Double)

object UserAccount{

  def apply(listOfInformation: List[String]): UserAccount = {
    UserAccount(listOfInformation.head.toLong, listOfInformation(1), listOfInformation(2),
      listOfInformation(3),listOfInformation.last.toDouble)
  }
}
