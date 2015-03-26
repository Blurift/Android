package com.game.screenManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keirron on 25/03/2015.
 */
public class ScreenManager {
    private List<Screen> screens = new ArrayList<Screen>();

    public ScreenManager()
    {

    }

    public void Render()
    {
        for (int i = screens.size()-1; i >= 0; i--)
        {
            screens.get(i).Update();
            screens.get(i).Render();

        }
    }

    public void Dispose()
    {
        for (int i = 0; i < screens.size(); i++)
        {
            screens.get(i).Dispose();
        }
    }

    ///Manipulation
    public void AddScreen(Screen screen)
    {
        if(!screens.contains(screen))
        {
            screens.add(screen);
        }
    }

    public void RemoveScreen(Screen screen)
    {
        if(screens.contains(screen))
        {
            screens.remove(screen);
        }
    }
}
