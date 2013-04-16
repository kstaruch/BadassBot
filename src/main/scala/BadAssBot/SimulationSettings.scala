package BadAssBot

/**
 * Contains parameters for various aspects of simulation
 */
case class SimulationSettings(
                               minionSpawnProbability: Double,
                               enemyMinionSpawnDistance: Int, enemyMinionSpawnProbability: Double,
                               slideLength: Int, energyGoal: Int,
                               suicideRange: Int, suicideProbability: Double,
                               roundsBeforeApocalypseSuicide: Int
                               )
