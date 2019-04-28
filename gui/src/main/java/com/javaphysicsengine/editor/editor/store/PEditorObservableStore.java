package com.javaphysicsengine.editor.editor.store;

import com.javaphysicsengine.api.body.PBody;

import java.util.ArrayList;
import java.util.List;

public class PEditorObservableStore extends PEditorStore {

    public interface AddBodyListener {
        void onAddBody(PBody body);
    }

    public interface DeleteBodyListener {
        void onDeleteBody(String objectName);
    }

    public interface ClearBodiesListener {
        void onClearBodies();
    }

    public interface SelectedBodyListener {
        void onSelectedBodyChanged(PBody newBody);
    }

    public interface ChangeBodyNameListener {
        void onChangedBodyName(PBody bodyWithNewName, String oldName);
    }

    private List<AddBodyListener> addBodyListeners = new ArrayList<>();
    private List<DeleteBodyListener> deleteBodyListeners = new ArrayList<>();
    private List<ClearBodiesListener> clearBodiesListeners = new ArrayList<>();
    private List<SelectedBodyListener> selectedBodyListeners = new ArrayList<>();
    private List<ChangeBodyNameListener> changeBodyNameListeners = new ArrayList<>();

    public List<DeleteBodyListener> getDeleteBodyListeners() {
        return deleteBodyListeners;
    }

    public List<AddBodyListener> getAddBodyListeners() {
        return addBodyListeners;
    }

    public List<ClearBodiesListener> getClearBodiesListeners() {
        return clearBodiesListeners;
    }

    public List<SelectedBodyListener> getSelectedBodyListeners() {
        return selectedBodyListeners;
    }

    public List<ChangeBodyNameListener> getChangeBodyNameListeners() {
        return changeBodyNameListeners;
    }

    @Override
    public void addBody(PBody body) {
        super.addBody(body);

        for (AddBodyListener addBodyListener : addBodyListeners) {
            addBodyListener.onAddBody(body);
        }
    }

    @Override
    public void deleteBody(String objectName) {
        super.deleteBody(objectName);

        for (DeleteBodyListener deleteBodyListener : deleteBodyListeners) {
            deleteBodyListener.onDeleteBody(objectName);
        }
    }

    @Override
    public void clearBodies() {
        super.clearBodies();

        for (ClearBodiesListener listeners : clearBodiesListeners) {
            listeners.onClearBodies();
        }
    }

    @Override
    public void setSelectedBody(PBody newSelectedBody) {
        super.setSelectedBody(newSelectedBody);

        for (SelectedBodyListener listener : selectedBodyListeners) {
            listener.onSelectedBodyChanged(newSelectedBody);
        }
    }

    @Override
    public void changeBodyName(String newName, PBody body) {
        String oldName = body.getName();
        super.changeBodyName(newName, body);

        for (ChangeBodyNameListener listener : changeBodyNameListeners) {
            listener.onChangedBodyName(body, oldName);
        }
    }
}
