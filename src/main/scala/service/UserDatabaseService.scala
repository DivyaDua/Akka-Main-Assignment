package service

import models.{UserAccount, UserDatabase}

object UserDatabaseService extends UserDatabase{

  def addAccount(userName: String, userAccount: UserAccount) = {
    userAccountMap += (userName -> userAccount)
  }

  def checkForUserNameUniqueness(userName: String) =
    if(!userAccountMap.contains(userName)) true  else false


}
