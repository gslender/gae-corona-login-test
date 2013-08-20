-----------------------------------------------------------------------------------------
--
-- main.lua
--
-----------------------------------------------------------------------------------------
--
-- change this to your app engine local or your apps at appspot.com
GAE_URL = "http://localhost:8888/"  -- or if hosted "http://[appid].appspot.com/" 


-- hide default status bar (iOS)
display.setStatusBar(display.HiddenStatusBar)

-- include Corona's widget library and set some screen defaults
widget = require "widget"
json = require "json"
dch =  display.contentHeight
dcw =  display.contentWidth

local storyboard = require "storyboard"
storyboard.gotoScene( "login" )

local maskBG
local imageSheet = graphics.newImageSheet( "loading.png", {width=128,height=128,numFrames=12} )
local sequenceData = {
    name="waiting",
    start=1,
    count=12,
    time=1000,        -- Optional. In ms.  
    loopCount = 0   -- Optional. Default is 0 (loop indefinitely)
}
local waitingSprite = display.newSprite(imageSheet, sequenceData )
waitingSprite.x = dcw/2
waitingSprite.y = dch/2
waitingSprite:play()
waitingSprite.isVisible = false

function nilTouchEvent() return true end

function showBusyWorking(show)
	if (show) then
		if maskBG then 
		    maskBG:removeEventListener("touch", nilTouchEvent)
		    maskBG:removeEventListener("tap", nilTouchEvent)
			maskBG:removeSelf() 
			maskBG = nil
		end
		maskBG = display.newRect(0, 0, dcw, dch)
		maskBG:setFillColor(0, 0, 0,128)		
	    maskBG:addEventListener("touch", nilTouchEvent)
	    maskBG:addEventListener("tap", nilTouchEvent)
		waitingSprite.isVisible = true
		waitingSprite:toFront()
	else
		if maskBG then 
			maskBG:removeSelf() 
			maskBG = nil
		end
		waitingSprite.isVisible = false
	end
end
    