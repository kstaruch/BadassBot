package BadAssBot

import BadAssBot.Goals._
import BadAssBot.Goals.RandomMinionSpawn
import BadAssBot.Goals.GoHome
import BadAssBot.Goals.Suicide

class MiniFoVBot extends FoVBot {
  override def allShortTermGoals: Seq[Goal] = ContinueSlide(10) :: GoHome(1400) :: Nil
  override def allActionGoals: Seq[Goal]  = Suicide(3) ::
    SuicideBeforeApocalypse(5) :: RandomMinionSpawn(0.8) :: EnemyProximityMinionSpawn(5) :: Nil
}