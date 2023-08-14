package com.elevator.system.common.domain;


import com.elevator.system.common.events.BaseEvent;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * this is an aggregator to save the state of the event
 */
public abstract class AggregateRoot {

    protected String id;
    private int version = 0;
    private boolean committed = false;

    private BaseEvent event =  new BaseEvent();
    private final Logger logger = Logger.getLogger(AggregateRoot.class.getName());

    protected void applyChange(BaseEvent event,Boolean isNewEvent){
        try {
            var method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);

        } catch (NoSuchMethodError e){
            logger.log(Level.WARNING,
                    MessageFormat.format(
                            "This method wasn't found for {0}",
                            event.getClass().getName()
                    ));
        } catch (Exception e){
            logger.log(Level.SEVERE, "Error applying event to aggregate", e);
        } finally {
            if(isNewEvent){
                this.event = event;
            }
        }
    }

    public String getId() {
        return id;
    }



    public int getVersion() {
        return version;
    }

    public void markChangesAsCommitted(){
        this.committed = true;
    }
    public void raiseEvent(BaseEvent event){
        applyChange(event, true);
    }

    public BaseEvent getEvent() {
        return event;
    }
}
