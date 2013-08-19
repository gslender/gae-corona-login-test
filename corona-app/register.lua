----------------------------------------------------------------------------------
--
-- register.lua
--
-- Scene that is shown when user selects register
--
----------------------------------------------------------------------------------

local storyboard = require( "storyboard" )
local registerscene = storyboard.newScene()

-- Called when the scene's view does not exist:
function registerscene:createScene( event )
	local group = self.view
		
	--group:insert(...)

end


-- Called immediately after scene has moved onscreen:
function registerscene:enterScene( event )
	local group = self.view	
	
end


-- Called when scene is about to move offscreen:
function registerscene:exitScene( event )
	local group = self.view
	
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