package com.langlearnquiz.frontend.handlers.callback.startmenu;

import com.langlearnquiz.frontend.handlers.callback.CallbackRequestHandler;
import com.langlearnquiz.frontend.handlers.message.commands.CommandRequestHandler;

/**
 * Interface to mark specific handlers as the start menu callback-based. Handlers implemented by this interface is
 * the routers to the existed {@link CommandRequestHandler} handlers. Available only from main menu
 */
public interface StartMenuCallbackRequestHandler extends CallbackRequestHandler {}
