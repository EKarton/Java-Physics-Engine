package com.javaphysicsengine.editor.editor.store;

import com.javaphysicsengine.api.body.PCircle;
import com.javaphysicsengine.utils.Vector;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PEditorObservableStoreTest {

    private PEditorObservableStore store;

    @Before
    public void setup() {
        store = new PEditorObservableStore();

        store.getAddBodyListeners().add(mock(PEditorObservableStore.AddBodyListener.class));
        store.getAddBodyListeners().add(mock(PEditorObservableStore.AddBodyListener.class));

        store.getChangeBodyNameListeners().add(mock(PEditorObservableStore.ChangeBodyNameListener.class));
        store.getChangeBodyNameListeners().add(mock(PEditorObservableStore.ChangeBodyNameListener.class));

        store.getClearBodiesListeners().add(mock(PEditorObservableStore.ClearBodiesListener.class));
        store.getClearBodiesListeners().add(mock(PEditorObservableStore.ClearBodiesListener.class));

        store.getDeleteBodyListeners().add(mock(PEditorObservableStore.DeleteBodyListener.class));
        store.getDeleteBodyListeners().add(mock(PEditorObservableStore.DeleteBodyListener.class));

        store.getSelectedBodyListeners().add(mock(PEditorObservableStore.SelectedBodyListener.class));
        store.getSelectedBodyListeners().add(mock(PEditorObservableStore.SelectedBodyListener.class));
    }

    @Test
    public void addBody_should_call_all_listeners() {
        PCircle circle = new PCircle("My circle");
        circle.setRadius(10);
        circle.setCenterPt(new Vector(10, 10));

        store.addBody(circle);

        for (PEditorObservableStore.AddBodyListener listener : store.getAddBodyListeners()) {
            verify(listener, times(1)).onAddBody(eq(circle));
        }
    }

    @Test
    public void deleteBody_should_call_all_listeners() {
        PCircle circle = new PCircle("My circle");
        circle.setRadius(10);
        circle.setCenterPt(new Vector(10, 10));

        store.addBody(circle);
        store.deleteBody("My circle");

        for (PEditorObservableStore.DeleteBodyListener listener : store.getDeleteBodyListeners()) {
            verify(listener, times(1)).onDeleteBody(eq("My circle"));
        }
    }

    @Test
    public void clearBodies_should_call_all_listeners() {
        store.clearBodies();

        for (PEditorObservableStore.ClearBodiesListener listener : store.getClearBodiesListeners()) {
            verify(listener, times(1)).onClearBodies();
        }
    }

    @Test
    public void setSelectedBody_should_call_all_listeners() {
        PCircle circle = new PCircle("My circle");
        circle.setRadius(10);
        circle.setCenterPt(new Vector(10, 10));

        store.addBody(circle);
        store.setSelectedBody(circle);

        for (PEditorObservableStore.SelectedBodyListener listener : store.getSelectedBodyListeners()) {
            verify(listener, times(1)).onSelectedBodyChanged(eq(circle));
        }
    }

    @Test
    public void changeBodyName_should_call_all_listeners() {
        PCircle circle = new PCircle("My circle");
        circle.setRadius(10);
        circle.setCenterPt(new Vector(10, 10));

        store.addBody(circle);
        store.changeBodyName("My new circle", circle);

        for (PEditorObservableStore.ChangeBodyNameListener listener : store.getChangeBodyNameListeners()) {
            verify(listener, times(1)).onChangedBodyName(eq(circle), eq("My circle"));
        }
    }
}