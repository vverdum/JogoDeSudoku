package br.com.dio.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.dio.service.EventNum.CLEAR_SPACE;

public class NotifierService {
    private final Map<EventNum, List<EventListener>> listeners = new HashMap<>(){
        {
            put(CLEAR_SPACE, new ArrayList<>());
        }
    };
    public void subscriber(final EventNum eventType, EventListener listener){
        var selectedListeners = listeners.get(eventType);
        selectedListeners.add(listener);
    }
    public void notify(final EventNum eventType){
        listeners.get(eventType).forEach(l ->l.update(eventType));
    }
}
