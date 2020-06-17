package com.inspirecoding.teki

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import java.util.*


class TekiClass: ApplicationAdapter()
{
    var screenWidth = 0
    var screenHeight = 0

    var batch: SpriteBatch? = null
    var shapeRenderer: ShapeRenderer? = null

    var gameState = 0

    //Initial screen
    var title: Texture? = null
    var play: Texture? = null
    var usernameButton: Texture? = null
    var highScore: Texture? = null

    //Username
    var username: BitmapFont? = null
    var usernameText: String = "Enter your username"

    //High Score screen
    var highScoreTitle: Texture? = null
    var score_highscore: BitmapFont? = null
    var username_highscore: BitmapFont? = null

    //High Scores
    var highscoresList = mutableListOf<Highscore>()

    // Background
    var backgrounds = mutableListOf<Texture>()
    val numberOfBackgrounds = 3
    var backgroundX = FloatArray(numberOfBackgrounds)

    // Teki
    var tekisLegStates = mutableListOf<Texture>()
    var tekisStates = 0
    var tekiX = 0f
    var tekiY = 0f
    var tekisRect: Rectangle? = null

    //Timer
    var timer: Timer = Timer()
    var level = 0
    var nextLevel = 20

    var numbersTextures = mutableListOf<Texture>()

    //Score
    var score = 0
    var scoreTextTexture: Texture? = null

    //Level
    var levelTextTexture: Texture? = null

    //Up and Down Arrow
    var upArrowTexture: Texture? = null
    var downArrowTexture: Texture? = null

    //Jump and crawl
    var jumpDistance = 0f
    var jumpPushed = false
    var jumpVelocity = 0f
    var crawlPushed = false

    //Barriers
    var downFlapBirdsAndStones = mutableListOf<Texture>()
    var upFlapBirdsAndStones = mutableListOf<Texture>()
    var numberOfBarriers = 10
    val barrierX = mutableListOf<Float>()
    val barrierY = mutableListOf<Float>()
    val barrierYPositions = mutableListOf<Float>()
    var randomBarrier = mutableListOf<Int>()
    var constOfJumpCrawlDist = 0
    var minBarrierOffset = 0f
    var birdsFlapState = 0
    var random = 0
    var barriersRect: Rectangle? = null

    //Game Over
    var gameOver: Texture? = null

    //Restart
    var restart: Texture? = null
    //Back to Main Menu
    var backToMainMenu: Texture? = null

    var gravity = 0.0f

    var distance = 0

    var velocity = 12

    var size: Float = 0f

    var prefs: Preferences? = null


    override fun create()
    {
        screenWidth = Gdx.graphics.width
        screenHeight = Gdx.graphics.height

        batch = SpriteBatch()
        shapeRenderer = ShapeRenderer()

        prefs = Gdx.app.getPreferences("My Preferences")

        getHighscores()

        size = (screenWidth*0.06).toFloat()
        gravity = (size*0.01).toFloat()
        constOfJumpCrawlDist = (size*8).toInt()
        minBarrierOffset = size*2

        //Get preferences
        usernameText = prefs!!.getString("currentUser", "Enter your username")

        //Initial screen
        title = Texture("tekiTitle.png")
        play = Texture("playButton.png")
        usernameButton = Texture("usernameButton.png")
        highScore = Texture("highScoreButton.png")

        //Username
        username = BitmapFont()
        username!!.setColor(Color.WHITE)
        username!!.data.scale(2f)

        //High Score screen
        highScoreTitle = Texture("highScoreButton.png")
        score_highscore = BitmapFont()
        score_highscore!!.setColor(Color.WHITE)
        score_highscore!!.data.scale(2f)
        username_highscore = BitmapFont()
        username_highscore!!.setColor(Color.WHITE)
        username_highscore!!.data.scale(2f)

        //Background
        for(i in 1..numberOfBackgrounds)
        {
            backgrounds.add(Texture("background_${i}.png"))
        }

        //Teki
        tekisLegStates.add(Texture("teki_legsOnTheGround.png")) //0
        tekisLegStates.add(Texture("teki_rightLegUp.png")) //1
        tekisLegStates.add(Texture("teki_leftLegUp.png")) //2
        tekisLegStates.add(Texture("teki_basic_jump.png")) //3
        tekiX = screenWidth*0.35f
        tekisRect = Rectangle()

        //Scoring
        for(i in 0..9)
        {
            numbersTextures.add(Texture("${i}.png"))
        }
        scoreTextTexture = Texture("score.png")

        //Level
        levelTextTexture = Texture("level.png")

        //Up and Down Arrow
        upArrowTexture = Texture("upArrow.png")
        downArrowTexture = Texture("downArrow.png")

        //Up and down flap Bird & Stones
        downFlapBirdsAndStones.add(Texture("bird_11.png")) //0
        downFlapBirdsAndStones.add(Texture("bird_21.png")) //1
        downFlapBirdsAndStones.add(Texture("bird_31.png")) //2
        downFlapBirdsAndStones.add(Texture("bird_41.png")) //3
        downFlapBirdsAndStones.add(Texture("bird_51.png")) //4
        downFlapBirdsAndStones.add(Texture("bird_61.png")) //5
        downFlapBirdsAndStones.add(Texture("stone_1.png")) //6
        downFlapBirdsAndStones.add(Texture("stone_2.png")) //7
        downFlapBirdsAndStones.add(Texture("stone_3.png")) //8
        downFlapBirdsAndStones.add(Texture("stone_4.png")) //9
        upFlapBirdsAndStones.add(Texture("bird_12.png"))
        upFlapBirdsAndStones.add(Texture("bird_22.png"))
        upFlapBirdsAndStones.add(Texture("bird_32.png"))
        upFlapBirdsAndStones.add(Texture("bird_42.png"))
        upFlapBirdsAndStones.add(Texture("bird_52.png"))
        upFlapBirdsAndStones.add(Texture("bird_62.png"))
        upFlapBirdsAndStones.add(Texture("stone_1.png"))
        upFlapBirdsAndStones.add(Texture("stone_2.png"))
        upFlapBirdsAndStones.add(Texture("stone_3.png"))
        upFlapBirdsAndStones.add(Texture("stone_4.png"))
        barriersRect = Rectangle()

        //Game Over
        gameOver = Texture(Gdx.files.internal("gameOver.png"))

        //Restart
        restart = Texture("restart.png")
        //Back to Main Menu
        backToMainMenu = Texture("back.png")



        resetGame()
    }

    fun resetGame()
    {
        // Background
        for (i in 0 until numberOfBackgrounds)
        {
            backgroundX[i] = screenWidth.toFloat() * i
        }

        //Teki
        tekiY = screenHeight*0.22f
        tekisStates = 0

        score = 0
        level = 0

        velocity = 12
        distance = 0

        jumpPushed = false
        jumpDistance = 0f
        jumpVelocity = (size*0.29).toFloat()
        crawlPushed = false

        //Barrier
        barrierYPositions.add(tekiY)
        barrierYPositions.add(tekiY + size*0.5f)
        barrierYPositions.add(tekiY + size*1.1f)
        barrierYPositions.add(tekiY + size*2f)
        barrierX.clear()
        barrierY.clear()
        randomBarrier.clear()
        for(i in 0 until numberOfBarriers)
        {
            val rand = Random().nextInt(10)
            if(i == 0)
            {
                random = Random().nextInt(3)
                barrierX.add(screenWidth + random*size)
                randomBarrier.add(rand)
            }
            else
            {
                random = Random().nextInt(3)
                barrierX.add(barrierX.max()!! + constOfJumpCrawlDist + minBarrierOffset + random*size)
                randomBarrier.add(rand)
            }
            if(rand in 6..9)
            {
                barrierY.add(barrierYPositions[0])
            }
            else
            {
                barrierY.add(barrierYPositions[Random().nextInt(4)])
            }
        }
    }

    override fun render()
    {
        batch!!.begin()
        shapeRenderer!!.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer!!.setColor(Color.BLUE)

        distance += velocity

        for(i in 0 until numberOfBackgrounds)
        {
            drawBackground(i)
        }

        if (gameState == 0) // Main Menu screen
        {
            drawTeki(0)
            drawTekiTitle()
            drawPlayButton()
            drawUsernameButton()
            drawUsername(usernameText)
            drawHighscoreButton()

            touchPlayButton()
            touchUsernameButton()
            touchHighscoreButton()
        }
        else if (gameState == 1) //Running game screen
        {
            //Background
            for(i in 0 until numberOfBackgrounds)
            {
                if (backgroundX[i] <= -screenWidth)
                {
                    if (i == 0)
                    {
                        backgroundX[i] = backgroundX.max()!!.plus(screenWidth) - velocity
                    }
                    else
                    {
                        backgroundX[i] = backgroundX.max()!!.plus(screenWidth)
                    }
                }
                else
                {
                    backgroundX[i] = backgroundX[i] - velocity
                }
            }

            //Teki
            if(distance % 20 == 0)
            {
                when(tekisStates)
                {
                    0 -> tekisStates = 1
                    1 -> tekisStates = 2
                    2 -> tekisStates = 1
                    3 -> tekisStates = 0
                }
            }

            if(jumpPushed)
            {
                handleJump()
            }
            else if(crawlPushed)
            {
                handleCrawl()
            }

            drawTeki(tekisStates)

            tekisRect!!.set(
                    tekiX,
                    tekiY,
                    size*1.2f,
                    size)

            touchUpAndDownArrow()

            //Barriers
            for ( i in 0 until numberOfBarriers)
            {
                val rand = Random().nextInt(10)
                if(barrierX[i] < -size*1.2f)
                {
                    random = Random().nextInt(3)
                    barrierX[i] = barrierX.max()!! + constOfJumpCrawlDist + minBarrierOffset + size*random
                    randomBarrier[i] = rand

                    if(rand in 6..9)
                    {
                        barrierY[i] = barrierYPositions[0]
                    }
                    else
                    {
                        barrierY[i] = barrierYPositions[Random().nextInt(4)]
                    }
                }
                else
                {
                    barrierX[i] = barrierX[i] - velocity
                }

                drawBarriers(i)

                barriersRect!!.set(
                        barrierX[i],
                        barrierY[i],
                        size*1.2f,
                        size)

                if (Intersector.overlaps(tekisRect, barriersRect))
                {
                    saveHighscores()
                    gameState = 2
                    stopTime()
                }
            }

            drawUpArrow()
            drawDownArrow()

            scoring()
            leveling()
        }
        else if (gameState == 2) //Game Over screen
        {
            drawTeki(tekisStates)
            drawGameOver()

            for (i in 0 until numberOfBarriers)
            {
                drawBarriers(i)
            }

            drawRestart()
            drawBackToMainMenu(
                    screenWidth-size*2 - size*1.5f,
                    screenHeight-size*4
            )

            touchRestartButton()
            touchBackToMainMenuButton(
                    screenWidth-size*3.5f+size*0.75f,
                    size*3.25f)

            scoring()
            leveling()
        }
        else if (gameState == 3) //High Score screen
        {
            drawHighscoreTitle()
            drawBackToMainMenu(
                    screenWidth - size*1.5f - size*0.5f,
                    size*1f)

            touchBackToMainMenuButton(
                    screenWidth - size*1.25f,
                    screenHeight - size*1.75f)

            for(i in 0..9)
            {
                if(highscoresList[i].username != "")
                {
                    drawHighscorePositions(
                            highscoresList[i].username,
                            highscoresList[i].score.toString(),
                            (i + 1).toFloat()
                    )
                }
            }
        }

        shapeRenderer!!.end()
        batch!!.end()
    }


    fun startTimer()
    {
        timer = Timer()

        timer.schedule(object : TimerTask()
        {
            override fun run()
            {
                if(score == nextLevel)
                {
                    level++
                    velocity += 2
                    nextLevel += 20
                }

                birdsFlapState = if (birdsFlapState == 0) {
                    1
                } else {
                    0
                }

                score++
            }
        }, 0, 1000)
    }
    fun stopTime()
    {
        timer.cancel()
    }

    fun handleJump()
    {
        tekisStates = 3
        jumpDistance += jumpVelocity
        if(jumpDistance in 0f..(size*4))
        {
            jumpVelocity -= gravity
            tekiY += jumpVelocity
        }
        else if(jumpDistance in (size*4 + 1)..(size*8))
        {
            jumpVelocity += gravity
            tekiY += -jumpVelocity
            if(jumpDistance >= (size*8).toInt())
            {
                jumpPushed = false
                jumpDistance = 0f
                tekiY = screenHeight*0.22f
            }
        }
        else
        {
            if(jumpDistance >= (size*8))
            {
                jumpPushed = false
                jumpDistance = 0f
                tekiY = screenHeight*0.22f
            }
        }
    }
    fun handleCrawl()
    {
        tekisStates = 3
        jumpDistance += jumpVelocity
        if(jumpDistance >= size*9)
        {
            crawlPushed = false
            jumpDistance = 0f
            tekiY = screenHeight*0.22f
        }
    }

    fun scoring()
    {
        batch!!.draw(
                scoreTextTexture,
                size,
                screenHeight - size*1.2f,
                size*2,
                size*0.8f)

        val scoreDigits = mutableListOf<Int>()
        var _score = score
        if (_score == 0)
        {
            batch!!.draw(
                    numbersTextures[0],
                    size*3 + size*0.5f,
                    screenHeight - size*1.2f,
                    size*0.5f,
                    size*0.8f)
        }
        else
        {
            while (_score > 0)
            {
                scoreDigits.add(_score % 10)
                _score /= 10
            }
            scoreDigits.reverse()

            for (i in 0 until scoreDigits.count())
            {
                batch!!.draw(
                        numbersTextures[scoreDigits[i]],
                        size*3 + size*0.5f + (size*0.5f)*i,
                        screenHeight - size*1.2f,
                        size*0.5f,
                        size*0.8f)
            }
        }
    }
    fun leveling()
    {
        batch!!.draw(
                levelTextTexture,
                size,
                screenHeight - size*1.2f - size,
                size*2*0.93f, //*0.93 - to don't be stretched
                size*0.8f)

        val scoreDigits = mutableListOf<Int>()
        var _level = level
        if (_level == 0)
        {
            batch!!.draw(
                    numbersTextures[0],
                    size + size*2*0.93f + size*0.5f,
                    screenHeight - size*1.2f - size,
                    size*0.5f,
                    size*0.8f)
            Gdx.app.log("leveling_1", "egy")
        }
        else
        {
            while (_level > 0)
            {
                scoreDigits.add(_level % 10)
                _level /= 10
            }
            scoreDigits.reverse()

            for (i in 0 until scoreDigits.count())
            {
                batch!!.draw(
                        numbersTextures[scoreDigits[i]],
                        size + size*2*0.93f + size*0.5f + (size*0.5f)*i,
                        screenHeight - size*1.2f - size,
                        size*0.5f,
                        size*0.8f)
            }
            Gdx.app.log("leveling_2", "kettő")
        }
    }
    fun touchUpAndDownArrow()
    {
        val upArrowCircle = Circle(
                size*2,
                screenHeight - (size*4 + size*0.5f),
                size)
        val downArrowCircle = Circle(
                size*2,
                screenHeight - (size*2),
                size)
        if(Gdx.input.justTouched())
        {
            //Touch of the upArrow
            if(upArrowCircle.contains(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat()))
            {
                Gdx.app.log("justTouched", "upArrow")
                if(!crawlPushed && !jumpPushed)
                {
                    jumpPushed = true
                }
            }
            //Touch of the downArrow
            if(downArrowCircle.contains(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat()))
            {
                Gdx.app.log("justTouched", "downArrow")
                if(!crawlPushed && !jumpPushed)
                {
                    crawlPushed = true
                    tekiY -= (size*0.55).toFloat()
                }
            }
        }
    }
    fun touchRestartButton()
    {
        val restartCircle = Circle(
                screenWidth-size*6+size*0.75f,
                size*3.25f,
                size*0.75f)

        if (Gdx.input.justTouched())
        {
            if(restartCircle.contains(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat()))
            {
                Gdx.app.log("restart", "restartCircle!")

                resetGame()
                gameState = 1
                startTimer()
            }
        }
    }
    fun touchBackToMainMenuButton(xPos: Float, yPos: Float)
    {
        val backToMainMenu = Circle(
                xPos,
                yPos,
                size*0.75f)

        if (Gdx.input.justTouched())
        {
            if(backToMainMenu.contains(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat()))
            {
                Gdx.app.log("restart", "backToMainMenu!")
                gameState = 0
                resetGame()
            }
        }
    }
    fun touchPlayButton()
    {
        val playButton = Rectangle(
                screenWidth-size*4f-size*0.635f,
                size*3.43f,
                size*2f-size*0.73f,
                size*0.8f+size*0.27f
        )

        if (Gdx.input.justTouched())
        {
            if(playButton.contains(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat()))
            {
                resetGame()
                gameState = 1
                startTimer()
            }
        }
    }
    fun touchUsernameButton()
    {
        val layout = GlyphLayout()
        layout.setText(username, usernameText)
        val height = layout.height
        Gdx.app.log("height", "$height")
        val usernameButton = Rectangle(
                screenWidth - size*4f - size*1.87f,
                size*5.4f - size*0.8f,
                size*3.74f,
                size*0.8f + height)

        if (Gdx.input.justTouched())
        {
            if(usernameButton.contains(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat()))
            {
                Gdx.input.getTextInput(object : Input.TextInputListener {
                    override fun input(text: String) {
                        Gdx.app.log("username", "$text / $size")
                        usernameText = text
                        prefs!!.putString("currentUser", text)
                        prefs!!.flush()
                    }

                    override fun canceled() {}
                }, "Username", "", "Enter your username")
            }
        }
    }
    fun touchHighscoreButton()
    {
        val highscoreButton = Rectangle(
                screenWidth - size*4f - size*1.87f,
                size*6.8f,
                size*3.74f,
                size*0.8f)

        if (Gdx.input.justTouched())
        {
            if(highscoreButton.contains(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat()))
            {
                Gdx.app.log("highScore", "${size*5.4f - size*0.8f}")
                gameState = 3
            }
        }
    }

    fun drawBarriers(numberOfBarrier: Int)
    {
        when(birdsFlapState)
        {
            0 -> {
                batch!!.draw(
                        downFlapBirdsAndStones[randomBarrier[numberOfBarrier]],
                        barrierX[numberOfBarrier],
                        barrierY[numberOfBarrier],
                        size*1.2f,
                        size)
            }
            1 -> {
                batch!!.draw(
                        upFlapBirdsAndStones[randomBarrier[numberOfBarrier]],
                        barrierX[numberOfBarrier],
                        barrierY[numberOfBarrier],
                        size*1.2f,
                        size)
            }
        }
    }
    fun drawTekiTitle()
    {
        batch!!.draw(
                title,
                screenWidth-size*6,
                screenHeight-size*2.5f,
                size*4,
                size*4*0.5f)
    }
    fun drawPlayButton()
    {
        batch!!.draw(
                play,
                screenWidth-size*4f-size*0.635f,
                screenHeight - size*4.5f,
                size*2f-size*0.73f,
                size*0.8f+size*0.27f)
    }
    fun drawUsernameButton()
    {
        batch!!.draw(
                usernameButton,
                screenWidth - size*4f - size*1.87f,
                screenHeight - size*4.5f - size*0.8f - size*0.1f,
                size*3.74f,
                size*0.8f)
    }
    fun drawUsername(usernameText: String)
    {
        username!!.draw(
                batch,
                usernameText,
                screenWidth - size*4f - size*1.87f,
                screenHeight - size*4.5f - size*0.8f - size*0.2f)
    }
    fun drawHighscoreButton()
    {
        batch!!.draw(
                highScore,
                screenWidth-size*4f-size*2.06f,
                screenHeight - size*4.5f - size*0.8f - (size*0.8f+size*0.27f) - size*0.4f - size,
                size*4.12f,
                size*0.8f+size*0.27f)
    }
    fun drawHighscoreTitle()
    {
        batch!!.draw(
                highScore,
                screenWidth-size*7,
                screenHeight - size*2f,
                size*4.12f,
                size*0.8f+size*0.27f)
    }
    fun drawBackground(number: Int)
    {
        // Background
        batch!!.draw(
                backgrounds[number],
                backgroundX[number],
                0f,
                screenWidth.toFloat(),
                screenHeight.toFloat())
    }
    fun drawTeki(number: Int)
    {
        //Teki
        batch!!.draw(
                tekisLegStates[number],
                tekiX,
                tekiY,
                size*1.2f,
                size)
    }
    fun drawUpArrow()
    {
        batch!!.draw(
                upArrowTexture,
                size,
                size*3 + size*0.5f,
                size*2,
                size*2)
    }
    fun drawDownArrow()
    {
        batch!!.draw(
                downArrowTexture,
                size,
                size,
                size*2,
                size*2)
    }
    fun drawGameOver()
    {
        batch!!.draw(
                gameOver,
                screenWidth-size*7,
                screenHeight-size*2,
                size*6,
                size)
    }
    fun drawRestart()
    {
        batch!!.draw(
                restart,
                screenWidth-size*6,
                screenHeight-size*4,
                size*1.5f,
                size*1.5f)
    }
    fun drawBackToMainMenu(xPos: Float, yPos: Float)
    {
        batch!!.draw(
                backToMainMenu,
                xPos,
                yPos,
                size*1.5f,
                size*1.5f)
    }
    fun drawHighscorePositions(username: String, score: String, position: Float)
    {
        score_highscore!!.draw(
                batch,
                score,
                screenWidth-size*7,
                (screenHeight - size*1.8f) - size*0.5f*position)
        username_highscore!!.draw(
                batch,
                username,
                screenWidth-size*6,
                (screenHeight - size*1.8f) - size*0.5f*position)
    }

    fun getHighscores()
    {
        for(i in 0..9)
        {
            val _username = prefs!!.getString("username+$i")
            val _score = prefs!!.getInteger("score+$i")
            highscoresList.add(Highscore(_username, _score))
        }
        highscoresList.sortByDescending { it.score }
    }
    fun saveHighscores()
    {
        if(usernameText != "" && usernameText != "Enter your username")
        {
            userFound@ for(i in 0 until highscoresList.size)
            {
                if(highscoresList[i].username == usernameText)
                {
                    if(highscoresList[i].score < score)
                    {
                        highscoresList[i].score = score
                    }
                    break@userFound
                }
                if (i == highscoresList.lastIndex)
                {
                    highscoresList.add(Highscore(
                            usernameText,
                            score
                    ))
                }
            }

            highscoresList.sortByDescending { it.score }

            for(i in 0 until highscoresList.size)
            {
                prefs!!.putString("username+$i", highscoresList[i].username)
                prefs!!.putInteger("score+$i", highscoresList[i].score)
                prefs!!.flush()
            }
        }
    }
}