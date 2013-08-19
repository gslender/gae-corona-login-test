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


    