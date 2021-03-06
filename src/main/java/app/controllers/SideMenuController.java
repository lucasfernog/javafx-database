package app.controllers;

import app.controllers.model.query.*;
import com.jfoenix.controls.JFXListView;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/side_menu.fxml")
public class SideMenuController {

    @FXMLViewFlowContext
    private ViewFlowContext mContext;

    @FXML
    private Node mVehicleItem;

    @FXML
    private Node mDriverItem;

    @FXML
    private Node mScheduleItem;

    @FXML
    private Node mRouteItem;

    @FXML
    private Node mUserItem;

    @FXML
    private Node mStudentItem;

    @FXML
    private JFXListView<Node> mSideList;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        FlowHandler contentFlowHandler = (FlowHandler) mContext.getRegisteredObject("ContentFlowHandler");

        mSideList.propagateMouseEventsToParent();
        mSideList.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    contentFlowHandler.handle(newVal.getId());
                } catch (VetoException | FlowException exc) {
                    exc.printStackTrace();
                }
            }
        });
        Flow contentFlow = (Flow) mContext.getRegisteredObject("ContentFlow");

        //Menu handler
        bindNodeToController(mScheduleItem, ScheduleController.class, contentFlow, contentFlowHandler);
        bindNodeToController(mRouteItem, RouteController.class, contentFlow, contentFlowHandler);
        bindNodeToController(mVehicleItem, VehicleController.class, contentFlow, contentFlowHandler);
        bindNodeToController(mDriverItem, DriverController.class, contentFlow, contentFlowHandler);
        bindNodeToController(mUserItem, UserController.class, contentFlow, contentFlowHandler);
        bindNodeToController(mStudentItem, StudentController.class, contentFlow, contentFlowHandler);
    }

    private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow, FlowHandler flowHandler) {
        flow.withGlobalLink(node.getId(), controllerClass);
    }

}