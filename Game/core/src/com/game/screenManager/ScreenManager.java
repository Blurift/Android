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

    public void render()
    {
        for (int i = screens.size()-1; i >= 0; i--)
        {
            screens.get(i).update();
            screens.get(i).render();

        }
    }

    public void dispose()
    {
        for (int i = 0; i < screens.size(); i++)
        {
            screens.get(i).dispose();
        }
    }

    ///Manipulation
    public void addScreen(Screen screen)
    {
        if(!screens.contains(screen))
        {
            screens.add(screen);
        }
    }

    public void removeScreen(Screen screen)
    {
        if(screens.contains(screen))
        {
            screens.remove(screen);
        }
    }
}
