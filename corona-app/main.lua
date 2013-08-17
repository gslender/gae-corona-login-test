-----------------------------------------------------------------------------------------
--
-- main.lua
--
-----------------------------------------------------------------------------------------
--
-- change this to your app engine local or your apps at appspot.com
GAE_URL = "http://gae-corona-login-test.appspot.com/login" -- or if local>> "http://localhost:8888/login" 

-- hide default status bar (iOS)
display.setStatusBar(display.HiddenStatusBar)

-- include Corona's widget library and set some screen defaults
local widget = require "widget"
local json = require "json"
local dch =  display.contentHeight
local dcw =  display.contentWidth

-- add a username label and text box
local username = display.newText("Username",dcw*.1,dch*.1, native.systemFontBold, 16)
local usernameTxtBox = native.newTextField( dcw*.1, dch*.15,dcw*.6, username.contentHeight+6 )
usernameTxtBox.font = native.newFont( native.systemFontBold, 16 )
usernameTxtBox.text = "user.name"

-- add a password label and text box
display.newText("Password",dcw*.1,dch*.3, native.systemFontBold, 16)
local passwordTxtBox = native.newTextField( dcw*.1, dch*.35,dcw*.6, username.contentHeight+6 )
passwordTxtBox.font = native.newFont( native.systemFontBold, 16 )
passwordTxtBox.text = "s3cr3t"

-- add a login button
local loginButton = widget.newButton( { width = 165, height = 50, label = "Login"})
loginButton.x = dcw*.5
loginButton.y = dch*.55

-- finally add a status message text
local statusMsg = display.newText("Press login button",0,0, native.systemFontBold, 16)
statusMsg:setReferencePoint(display.CenterReferencePoint)
statusMsg.x = dcw*.5
statusMsg.y = dch*.7
 
function handleButtonEvent( event )
	loginButton:setEnabled(false)
	loginButton.alpha = .5
	-- now setup some tables for headers & json data    
	local headers = {
		["Content-Type"] = "application/json"
	}
	
	local json_data = {
		["username"] = usernameTxtBox.text,
		["password"] = passwordTxtBox.text
	}
	
	-- add to a params table
	local params = {}
	params.headers = headers
	params.body = json.encode(json_data)
	
	-- this function handles when we get a response back from the http request
	local function handleResponse(event)
		if event.phase == "ended" then 
			loginButton:setEnabled(true)
			loginButton.alpha = 1
			if event.isError then
		        statusMsg.text = "Network Unavailable"
				return
			end
			-- we got this far so the network is good, just check the webservice
			if event.status == 200 then
				-- decode the response into a table
		        local loginResponse = json.decode(event.response)
		        statusMsg.text = loginResponse.message
		        statusMsg:setTextColor(180, 0, 0)
		        if loginResponse.status == "OK" then
		        	statusMsg:setTextColor(0, 180, 0)
		        end
		    else
		        statusMsg.text = "WebService Unavailable"
		    end
	    end
	end
	network.request( GAE_URL,"POST",handleResponse,params)   
	 
end
loginButton._view._onRelease = handleButtonEvent
    