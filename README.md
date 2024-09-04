**MAIN IDEA**

There are two sides which are called Zorde and Calliance. Main theme of the game is the war
between two sides. Each side has 3 different types of characters. The game is played on a
board where each character occupies a cell on the board (just like chess). During the progress
of the game, each side makes moves in turns. The aim of the game is to kill every opponent
character.

**Attack Powers and Health Points of characters:**

![1](https://github.com/MertKadakal/Pusu/blob/master/G%C3%B6rseller/karakter_%C3%B6zellikleri.png)

**Detailed attributes of characters:**

![2](https://github.com/MertKadakal/Pusu/blob/master/G%C3%B6rseller/karakter_detaylar%C4%B1.png)

**The board in the game:**

a - how it's printed on output file

b - how indexes of characters are counted

![3](https://github.com/MertKadakal/Pusu/blob/master/G%C3%B6rseller/tahta.png)

**COMBAT RULES**

        1. Characters cannot move onto the friendly characters (friendly character means characters
        that are at the same side). If a character tries to move onto a friendly character,
        its move is finalized at the current location and the rest of its move sequence will be
        ignored. For example if a character tries to move onto a friendly character at the second
        step of a 4-step move, only the first step of the move will be executed.
          
        2. Characters can move onto the enemy characters. If such a situation occurs, two enemy
        characters that are at the same cell fight to the death for the cell. Before a fight to
        the death, the attacking character will make a single attack to the defending character
        with its attack power. A fight to the death can be won by the character that has the
        highest current HP value but its HP value will decrease by an amount equal to the
        losing characters current HP value. For example, if a human (current HP : 100) moves
        onto a cell that is currently occupied by an ork (current HP : 60), the human will make
        an initial attack to the ork (now, the ork’s current HP is 30). Then, the ork will die
        and human’s HP value will decrease to 70.

        (a) The attack that is made before the fight to the death is not a normal attack. Thus,
        it will only effect the defending enemy character, not the other characters at the
        neighboring 8 cells.

        (b) After a fight to the death, the attacker will stop moving. For example if a character
        tries to move onto an enemy character at the second step of a 4-step move, even if
        the attacker is the winner of the fight to the death, only the first and second steps
        of the move will be executed. The rest of the move sequence will be ignored.

        (c) If two characters have same HP just before a fight to the death, they both die.

        3. Before they start a move sequence, Orks will first heal any friendly character in one of
        the neighboring cells.
        • Friendly characters will be healed by 10 hit points.
        • A character’s hit point cannot exceed its default hit point via healing. If this
        happens, you should decrease the current hit point to the default hit point of that
        character.
        • When healing the neighboring characters, Orks will heal themselves too.
        
        4. After they finish a move sequence, Elfs will make a ranged attack (instead of the final
        step’s default attack) to all enemy characters that are in range of 2 cells with an attack
        power of 15.
        • If an Elf’s move sequence is interrupted by moving through an enemy character,
        then this power cannot be used. This power can only be used if the Elf completes
        its whole move sequence without being interrupted.
        
        5. Game ends when all characters of one side is dead. When the game ends, you should
        add a line indicating the winner of the game as shown in the figure.

**The UML Class Diagram of the project:**

![4](https://github.com/MertKadakal/Pusu/blob/master/G%C3%B6rseller/UML_Diagram.png)
