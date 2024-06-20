/*

explode - modifying dice to become exploding dice [example: roll 2d20/exp]
penetrate - modifying dice to become penetrating dice [example: roll 3d8/pen-4]
drop lowest - drop specified number of dice with lowest number [example: roll 5d10/dl:2]
keep lowest - keep specified number of dice with lowest number [example: roll 10d4/kl:4]
drop highest - drop specified number of dice with highest number [example: roll 2d2+4d33/dh:3]
keep highest - keep specified number of dice with highest number [example: roll 5d33/kh:2]
reroll - reroll dice with result on value or lower [example: roll 6d10/rr:1]
multiplier - multiply the number of dice rolls by the value [example: roll 2d20/x:3]
minimum - set minimum value for dice roll result [example: roll 6d20/min:10]
counter - count results with value or higher [example: roll 10d8/c:7]
success - count results with value of higher as +1 (success) and 1 as -1 (
fail) [example: roll 7d12/suc:10]
additive - add value to each dice roll result [example: roll 2d20/add:4]
subtraction - subtract value from each dice roll result [example: roll d100/sub:10]
divisor - divides the roll result by the value and rounds down [example: roll 4d8/div:2]
multiplier - multiply result [example: roll 3d4/mul:2]

=======================

Roll Examples

Below are examples of the dice roll syntax:

/roll 2d6 + 3d10: Roll two six-sided dice and three ten-sided dice.

/roll 3d6 + 5: Roll three six-sided dice and add five. Other supported static modifiers are add (+),
subtract (-), multiply (*), and divide (/). NOTE: These static modifiers must be applied to the end
of the roll syntax and after other modifiers.

/roll 3d6 e6: Roll three six-sided dice and explode on sixes. Some game systems call this 'open
ended' dice. If the number rolled is greater than or equal to the value given for this option, the
die is rolled again and added to the total. If no number is given for this option, it is assumed to
be the same as the number of sides on the die. Thus, '3d6 e' is the same as '3d6 e6'. The dice will
only explode once with this command. Use "ie" for indefinite explosions.

/roll 3d6 ie6: Roll three six-sided dice and explode on sixes indefinitely within reason. We will
cap explosions at 100 rolls to prevent abuse.

/roll 3d10 d1: Roll three ten-sided dice and drop one die. The lowest value will be dropped first.
NOTE: These dice are dropped before any dice are kept with the following k command. Order of
operations is : roll dice, drop dice, keep dice

/roll 3d10 k2: Roll three ten-sided dice and keep two. The highest value rolled will be kept.

/roll 4d6 r2: Roll four six-sided dice and reroll any that are equal to or less than two once. Use
ir for indefinite rerolls.

/roll 6d10 t7: Roll six ten-sided dice and any that are seven or higher are counted as a success.
The dice in the roll are not added together for a total. Any die that meets or exceeds the target
number is added to a total of successes.

/roll 4d6 ir2: Roll four six-sided dice and reroll any that are equal to or less than two (and do
the same to those dice). This is capped at 100 rerolls to prevent abuse.

/roll 5d10 t8 f1: f# denotes a failure number that each dice must match or be beneath in order to
count against successes. These work as a sort of negative success and are totaled together as
described above. In the example roll, roll five ten-sided dice and each dice that is 8 or higher is
a success and subtract each one. The total may be negative.

/roll 4d10 kl3: Roll four ten-sided dice and keep the lowest three dice rolled. NOTE: This modifier
will only work with comments and math modifiers

/roll purge 10: Purge the last 10 messages from channel. The purge value can be between 2 to 100
messages and requires the user to have the "manage messages" role.

/roll 4d6 ! Hello World!: Roll four six-sided dice and add comment to the roll.

/roll 6 4d6: Roll 6 sets of four sixe-sided dice. A size of a set can be between 2 and 20.

/roll s 4d6: Simplify roll output by not showing the tally.

/roll 4d6 ! unsort or !roll ul 4d6: Roll four six-sided dice and unsort the tally.

/roll help: Displays basic usage instructions.

/roll help alias: Displays alias instructions.

/roll help system: Displays game system instructions.

/roll donate: Get donation information on how to help support the bot!

These commands can be combined. For example:

/roll 10d6 e6 k8 +4: Roll ten six-sided dice , explode on sixes and keep eight of the highest rolls
and add four.
Game Systems Specific Rolls

Warhammer 40k Wrath and Glory example syntaxes:

/roll wng 4d6: Roll four six-sided dice with a wrath dice.

/roll wng dn2 4d6: Roll four six-sided dice with a wrath dice and a difficulty test of 2. The bot
will append the test pass/fail status to the roll result!

/roll wng 4d6 !soak or /roll wng 4d6 !exempt or /roll wng 4d6 !dmg: Roll four six-sided dice without
a wrath dice.

Dark Heresy 2nd edition syntaxes:

/roll dh 4d10: Roll four ten-sided dice for dark heresy 2nd edition. If your roll contains a natural
10, you will be prompted with a righteous fury notification!
Alias Rolls

Alias rolls are commands that are shorthand for a longer, more complex comand. They can also change
what the dice faces appear as in most cases. Below is the complete list of aliases , with example
rolls, currently supported by Dice Maiden. Have a game system that you want turned into an alias?
Create an issue on github to get it added to this list!

4cod -> 4d10 t8 ie10 Chronicles of Darkness. The first number is the number of dice to roll.

4wod8 -> 4d10 f1 ie10 t8 World of darkness 4th edition. The first number is the number of dice to
roll and the second is the toughness of the check. Exploding 10s will be added to this alias at a
later date.

3df -> 3d3 t3 f1 Fudge dice from the fate RPG system. The number represents total dice rolled. This
alias also outputs the dice faces as +/ /-.

3wh4+ -> 3d6 t4 Warhammer Age of Sigmar/40k style rolls. The first number is the amount of dice
rolled and the second number is the target number.

dd34 -> (1d3 * 10) + 1d4 Double digit rolls. Uses the first number for the first digit and the
second number for the second digit. This is sometimes used in warhammer as a "d66".

age -> 2d6 + 1d6 AGE system roll. The AGE system games use 3d6 for ability tests, with 1 of them
typically being represented as a drama die, stunt die, dragon die etc. It is important that all
three dice be rolled together but the drama die is able to be distinguished from the others. Example
games include Fantasy Age, Dragon Age, Mordern Age, Blue Rose, and The Expanse RPG.

+dX -> 2dX d1 Advantage roll where X is the dice sides value. Roll two dice and keep the highest.

-dX -> 2dX kl1 Disadvantage roll where X the dice sides value. Roll two dice and keep the lowest.

+d% -> ((2d10kl1-1) *10) + 1d10 Advantage roll for a percentile dice in a roll-under system. Rolls
two tens and keeps the lowest.

-d% -> ((2d10k1-1) *10) + 1d10 Disadvantage roll for a percentile dice in a roll-under system. Rolls
two tens and keeps the highest.

snm5 -> 5d6 ie6 t4 Sunsails: New Millennium 4th edition. The number represents total dice rolled.
Indefinitely explodes on sixes, with a target of four.

d6s4 -> 4d6 + 1d6 ie The D6 System. The number must be 1 lower than the total size of the dice pool
as the wild die is automatically added for you. If you have some pips to add put them on the end (
i.e. d6s4 +2 is the same as 4d6 + 1d6 ie + 2).

sr6 -> 6d6 t5 Shadowrun System. The number represents total dice rolled. Target to hit is 5 or
higher.

sp4 -> 4d10 t8 ie10 Storypath system. The number represents total dice rolled. A d10 system with a
target set to 8 and infinite explosion on 10.

=======================

Function Reference
Please feel free to contact me to improve this section or to suggest new functions.

Also, check the G+ community to ask for help, send suggestion and read latest news about Quick Dice
Roller.

Absolute - Returns the absolute of a value.
abs(param)
Aeon - Each roll can explode to two or more rolls.
aeon(roll, poolSize, target[, branches])
BASH! - This function is used to support BASH! rules.
bash(poolSize, roll)
Burning Wheel - This function is used to support Burning Wheel rules.
bwheel(roll, poolSize, target, rollAgain)
Dice Pool - Roll a number of dice, count results above a certain target.
pool(roll, poolSize, target[, extra[, fail[, explode[, limit]]]])
Dystopian Wars - This function is used to support Dystopian Wars rules.
dwars(roll, poolSize, target, rollAgain)
Exalted - Makes a check using Exalted rules.
exal(poolSize, target)
Exploding - Makes an exploding roll.
expUp(roll[, target[, newRoll[, newTarget[, limit]]]])
Exploding/Collapsing - Make an exploding/collapsing roll.
exp(roll[, upperTarget[, lowerTarget[, upperLimit[, lowerLimit]]]])
HERO System - Compute Normal Damage and Body Damage according to the HERO System.
hero(poolSize)
Maximum - Return the highest of two values.
max(firstRoll, secondRoll)
Minimum - Return the lowest of two values.
min(firstRoll, secondRoll)
New WoD - Makes a check using the New World of Darkness rules.
nwod(poolSize, target)
Original WoD - Makes a check using the Original World of Darkness rules.
owod(poolSize, target)
Random - Generate a random value.
rand(min, max)
Rolemaster - Roll can explode accroding to Rolemaster rules.
rolemaster(roll[, target[, fumble[, limit]]])
Roll again - Roll again if result is equal or below the boundary.
reroll(roll, boundary, maxRoll)
Roll and Keep - Execute a Roll & Keep test.
rak(roll, poolSize[, keep])
Round down - Rounds down a value.
rdn(value)
Round up - Rounds up a value.
rup(value)
Shadowrun 4 - Perform a test using the Shadowrun 4ed system.
shrun4(roll, poolSize, target, rollAgain, failTarget)
Shadowrun 5 - Perform a test using the Shadowrun 5ed system.
shrun5(roll, poolSize, target, rollAgain, failTarget)

=========================