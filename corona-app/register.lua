----------------------------------------------------------------------------------
--
-- register.lua
--
-- Scene that is shown when user selects register
--
----------------------------------------------------------------------------------

local storyboard = require( "storyboard" )
local registerscene = storyboard.newScene()
local nameTxtBox
local emailTxtBox
local password1TxtBox
local password2TxtBox

-- Called when the scene's view does not exist:
function registerscene:createScene( event )
	local group = self.view
	
	-- add a name label and text box
	local name = display.newText("Your Name",dcw*.1,dch*.05, native.systemFontBold, 16)
	nameTxtBox = native.newTextField(dcw*.1, dch*.1,dcw*.7, name.contentHeight+6 )
	nameTxtBox.font = native.newFont(native.systemFontBold, 16 )
	nameTxtBox.text = "Your Full Name"
	
	-- add a email label and text box
	local email = display.newText("Your Email Address",dcw*.1,dch*.2, native.systemFontBold, 16)
	emailTxtBox = native.newTextField(dcw*.1, dch*.25,dcw*.8, name.contentHeight+6 )
	emailTxtBox.font = native.newFont(native.systemFontBold, 16 )
	emailTxtBox.text = "you@email.com"
	
	-- add a pwd1 label and text box
	local password1 = display.newText("Type a password",dcw*.1,dch*.35, native.systemFontBold, 16)
	password1TxtBox = native.newTextField(dcw*.1, dch*.4,dcw*.6, name.contentHeight+6 )
	password1TxtBox.font = native.newFont(native.systemFontBold, 16 )
	password1TxtBox.text = "secret"
	password1TxtBox.isSecure = true
	
	-- add a pwd2 label and text box
	local password2 = display.newText("Confirm password",dcw*.1,dch*.5, native.systemFontBold, 16)
	password2TxtBox = native.newTextField(dcw*.1, dch*.55,dcw*.6, name.contentHeight+6 )
	password2TxtBox.font = native.newFont(native.systemFontBold, 16 )
	password2TxtBox.text = "secret"
	password2TxtBox.isSecure = true
	
	-- add a status message/instruction text
	local statusMsg = display.newText("Enter details & Submit to register...",dcw*.05,dch*.7, native.systemFontBold, 16)
	
	-- add a submit button
	local submitButton = widget.newButton({ width = 135, height = 50, label = "Submit"})
	submitButton.x = dcw*.75
	submitButton.y = dch*.9
	
	function handleSubmitButtonEvent( event )	
		showBusyWorking(true)		
		-- now setup some tables for headers & json data    
		local headers = {
			["Content-Type"] = "application/json"
		}
		
		local json_data = {
			["fullname"] = nameTxtBox.text,
			["password"] = password1TxtBox.text,
			["email"] = emailTxtBox.text
		}
		
		-- add to a params table
		local params = {}
		params.headers = headers
		params.body = json.encode(json_data)
		
		-- this function handles when we get a response back from the http request
		local function handleResponse(event)
			if event.phase == "ended" then 
				showBusyWorking(false)
				if event.isError then
			        statusMsg.text = "Network Unavailable"
					return
				end
				-- we got this far so the network is good, just check the webservice
				if event.status == 200 then
					-- decode the response into a table
			        local webServResp = json.decode(event.response)
--			        statusMsg.text = webServResp.message
--			        statusMsg:setTextColor(180, 0, 0)
			        if webServResp.status == "OK" then
			        	statusMsg:setTextColor(0, 180, 0)
			        end
			    else
			        statusMsg.text = "WebService Unavailable"
			    end
		    end
		end
		network.request( GAE_URL.."register","POST",handleResponse,params)   
	end
	submitButton._view._onRelease = handleSubmitButtonEvent
	
	-- add a back button
	local backButton = widget.newButton({ width = 135, height = 50, label = "...Back"})
	backButton.x = dcw*.25
	backButton.y = dch*.9
	
	function handleBackButtonEvent( event )		 
		storyboard.gotoScene( "login" )
	end
	backButton._view._onRelease = handleBackButtonEvent
		
	group:insert(name)
	group:insert(nameTxtBox)
	group:insert(email)
	group:insert(emailTxtBox)
	group:insert(password1)
	group:insert(password1TxtBox)
	group:insert(password2)
	group:insert(password2TxtBox)
	group:insert(statusMsg)	
	group:insert(submitButton)
	group:insert(backButton)

end


-- Called immediately after scene has moved onscreen:
function registerscene:enterScene( event )
	local group = self.view	
	
    if nameTxtBox then
        nameTxtBox.isVisible = true
    end
	
    if emailTxtBox then
        emailTxtBox.isVisible = true
    end
	
    if password1TxtBox then
        password1TxtBox.isVisible = true
    end
	
    if password2TxtBox then
        password2TxtBox.isVisible = true
    end
	
end


-- Called when scene is about to move offscreen:
function registerscene:exitScene( event )
	local group = self.view
	
    if nameTxtBox then
        nameTxtBox.isVisible = false
    end
	
    if emailTxtBox then
        emailTxtBox.isVisible = false
    end
	
    if password1TxtBox then
        password1TxtBox.isVisible = false
    end
	
    if password2TxtBox then
        password2TxtBox.isVisible = false
    end

    --best to remove keyboard focus and hide keyboard 
    native.setKeyboardFocus(nil)
end


-- Called prior to the removal of scene's "view" (display group)
function registerscene:destroyScene( event )
	local group = self.view	
end

registerscene:addEventListener( "createScene", registerscene )
registerscene:addEventListener( "enterScene", registerscene )
registerscene:addEventListener( "exitScene", registerscene )
registerscene:addEventListener( "destroyScene", registerscene )

return registerscene