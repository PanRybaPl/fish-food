/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.panryba.mc.food;

import java.util.Map;

public interface Spawner {
    void init(Map<String, Object> config);
    void spawn();
    int getTicks();
}
