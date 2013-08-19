----------------------------------------------------------------------------------
--
-- login.lua
--
-- Scene that is first shown to either complete login, or go register
--
----------------------------------------------------------------------------------

local storyboard = require( "storyboard" )
local loginscene = storyboard.newScene()
local passwordTxtBox
local usernameTxtBox

-- Called when the scene's view does not exist:
function loginscene:createScene( event )
	local group = self.view
	
	-- add a username label and text box
	local username = display.newText("Username",dcw*.1,dch*.05, native.systemFontBold, 16)
	usernameTxtBox = native.newTextField(dcw*.1, dch*.1,dcw*.6, username.contentHeight+6 )
	usernameTxtBox.font = native.newFont(native.systemFontBold, 16 )
	usernameTxtBox.text = "user.name"
	
	-- add a password label and text box
	local password = display.newText("Password",dcw*.1,dch*.2, native.systemFontBold, 16)
	passwordTxtBox = native.newTextField(dcw*.1, dch*.25,dcw*.6, username.contentHeight+6 )
	passwordTxtBox.font = native.newFont( native.systemFontBold, 16 )
	passwordTxtBox.text = "s3cr3t"
	
	-- add a login button
	local loginButton = widget.newButton({ width = 165, height = 50, label = "Login"})
	loginButton.x = dcw*.5
	loginButton.y = dch*.4
	
	-- finally add a status message text
	local statusMsg = display.newText("Enter details and Login, or ...",0,0, native.systemFontBold, 16)
	statusMsg:setReferencePoint(display.CenterReferencePoint)
	statusMsg.x = dcw*.5
	statusMsg.y = dch*.5
	
	-- add a register button
	local registerButton = widget.newButton({ width = 165, height = 50, label = "Register"})
	registerButton.x = dcw*.5
	registerButton.y = dch*.75
	 
	function handleLoginButtonEvent( event )
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
		network.request( GAE_URL.."login","POST",handleResponse,params)   
		 
	end
	loginButton._view._onRelease = handleLoginButtonEvent
	
	function handleRegisterButtonEvent( event )		 
		storyboard.gotoScene( "register" )
	end
	registerButton._view._onRelease = handleRegisterButtonEvent
	
	group:insert(username)
	group:insert(usernameTxtBox)
	group:insert(password)
	group:insert(passwordTxtBox)
	group:insert(loginButton)
	group:insert(statusMsg)
	group:insert(registerButton)

end


-- Called immediately after scene has moved onscreen:
function loginscene:enterScene( event )
	local group = self.view	
	
    if usernameTxtBox then
        usernameTxtBox.isVisible = true
    end
	
    if passwordTxtBox then
        passwordTxtBox.isVisible = true
    end
end


-- Called when scene is about to move offscreen:
function loginscene:exitScene( event )
	local group = self.view
	
    if usernameTxtBox then
        usernameTxtBox.isVisible = false
    end
	
    if passwordTxtBox then
        passwordTxtBox.isVisible = false
    end

    --best to remove keyboard focus and hide keyboard 
    native.setKeyboardFocus(nil)
end


-- Called prior to the removal of scene's "view" (display group)
function loginscene:destroyScene( event )
	local group = self.view	
end

loginscene:addEventListener( "createScene", loginscene )
loginscene:addEventListener( "enterScene", loginscene )
loginscene:addEventListener( "exitScene", loginscene )
loginscene:addEventListener( "destroyScene", loginscene )

return loginscene