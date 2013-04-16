package BadAssBot

import BadAssBot.Goals._
import BadAssBot.Goals.RandomMinionSpawn
import BadAssBot.Goals.GoHome
import BadAssBot.Goals.Suicide

class MiniFoVBot(settings: SimulationSettings) extends FoVBot(settings) {
  override def allShortTermGoals: Seq[Goal] =
    ContinueSlide(settings.slideLength) :: GoHome(settings.energyGoal) :: SeekEnemy(settings.enemyMinionSpawnDistance) :: Nil
  override def allActionGoals: Seq[Goal]  = Suicide(settings.suicideRange) ::
    SuicideBeforeApocalypse(settings.roundsBeforeApocalypseSuicide) ::
    RandomMinionSpawn(settings.minionSpawnProbability) ::
    EnemyProximityMinionSpawn(settings.enemyMinionSpawnDistance, settings.enemyMinionSpawnProbability) :: Nil
}