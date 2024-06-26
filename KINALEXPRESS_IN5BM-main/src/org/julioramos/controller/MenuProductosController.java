/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.julioramos.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.julioramos.bean.Producto;
import org.julioramos.bean.Proveedor;
import org.julioramos.bean.TipoDeProducto;
import org.julioramos.db.Conexion;
import org.julioramos.system.Principal;

/**
 *
 * @author lphg3
 */
public class MenuProductosController implements Initializable{

    
    private Principal escenarioPrincipal;
    private enum operaciones{AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Producto> listaProductos;
    private ObservableList <Proveedor> listaProveedores;
    private ObservableList <TipoDeProducto> listaTipoDeProducto;
    
    @FXML private TextField txtCodigoProd;
    @FXML private TextField txtDescPro;
    @FXML private TextField txtPrecioU;
    @FXML private TextField txtPrecioD;
    @FXML private TextField txtPrecioM;
    @FXML private TextField txtExistencia;
    @FXML private ComboBox cmbCodigoTipoP;
    @FXML private ComboBox cmbCodProv;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodProd;
    @FXML private TableColumn colDescProd;
    @FXML private TableColumn colPrecioU;
    @FXML private TableColumn colPrecioD;
    @FXML private TableColumn colPrecioM;
    @FXML private TableColumn colExistencia;
    @FXML private TableColumn colCodTipoProd;
    @FXML private TableColumn colCodProv;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargaDatos();
        cmbCodigoTipoP.setItems(getTipoP());
        cmbCodProv.setItems(getProveedores());
    }


    
    public void cargaDatos(){
    tblProductos.setItems(getProducto());
    colCodProd.setCellValueFactory(new PropertyValueFactory<Producto, String>("codigoProducto"));
    colDescProd.setCellValueFactory(new PropertyValueFactory<Producto, String>("descripcionProducto"));
    colPrecioU.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioUnitario"));
    colPrecioD.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioDocena"));
    colPrecioM.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioMayor"));
    colExistencia.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("existencia"));
    colCodTipoProd.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoTipoProducto"));
    colCodProv.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProveedor"));
    
    
    }
    public void selecionarElementos(){
       txtCodigoProd.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
       txtDescPro.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
       txtPrecioU.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
       txtPrecioD.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
       txtPrecioM.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
       txtExistencia.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
       cmbCodigoTipoP.getSelectionModel().select(buscarTipoProducto(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
    }
    
    public TipoDeProducto buscarTipoProducto (int codigoTipoProducto ){
        TipoDeProducto resultado = null;
        try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarTipoProducto(?)}");
         procedimiento.setInt(1, codigoTipoProducto);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new TipoDeProducto(registro.getInt("codigoTipoProducto"),
                                            registro.getString("descripcionProducto")
             
             );
         }
        }catch (Exception e)
        {
            e.printStackTrace();
        }    
    
        return resultado;
    }
    
    
    public ObservableList<Producto> getProducto(){
    ArrayList<Producto> lista = new ArrayList<Producto>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarProductos()}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Producto (resultado.getString("codigoProducto"),
                                    resultado.getString("descripcionProducto"),
                                    resultado.getDouble("precioUnitario"),
                                    resultado.getDouble("precioDocena"),
                                    resultado.getDouble("precioMayor"),
                                    resultado.getInt("existencia"),
                                    resultado.getInt("codigoTipoProducto"),
                                    resultado.getInt("codigoProveedor")            
            ));
        }
    }catch (Exception e){
        e.printStackTrace();
    }
    
    
    return listaProductos = FXCollections.observableArrayList(lista);
        
    }
    public ObservableList<Proveedor> getProveedores() {
        ArrayList<Proveedor> listaPro = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarproveedor()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaPro.add(new Proveedor(resultado.getInt("codigoProveedor"),
                        resultado.getString("NITProveedor"),
                        resultado.getString("nombresProveedor"),
                        resultado.getString("apellidosProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("contactoPrincipal"),
                        resultado.getString("paginaWeb"),
                        resultado.getString("telefonoProveedor"),
                        resultado.getString("emailProveedor")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProveedores = FXCollections.observableList(listaPro);
    }
     public ObservableList<TipoDeProducto> getTipoP() {
        ArrayList<TipoDeProducto> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarTipoProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoDeProducto(resultado.getInt("CodigoTipoProducto"),
                        resultado.getString("descripcionProducto")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoDeProducto = FXCollections.observableList(lista);
    }
    
     public void agregar (){
         switch(tipoDeOperacion){
             case NINGUNO:
              activarControles();
             btnAgregar.setText("Guardar");
             btnEliminar.setText("Cancelar");
             btnEditar.setDisable(true);
             btnReporte.setDisable(true);   
             tipoDeOperacion = operaciones.ACTUALIZAR;
             break;
             case ACTUALIZAR:
             guardar ();
             desactivarControles();
             limpiarControles ();
             btnAgregar.setText("Agregar");
             btnEliminar.setText("Eliminar");
             btnEditar.setDisable(false);
             btnReporte.setDisable(false);
             tipoDeOperacion = operaciones.NINGUNO;
             cargaDatos();
             break;
         }
     
     
     
     
     }
     
     
     public void guardar (){
         Producto registro = new Producto();
         registro.setCodigoProducto(txtCodigoProd.getText());
         registro.setCodigoProveedor(((Proveedor)cmbCodProv.getSelectionModel().getSelectedItem()
                 ).getCodigoProveedor());
         registro.setCodigoTipoProducto(((TipoDeProducto)cmbCodigoTipoP.getSelectionModel().getSelectedItem())
                 .getCodigoTipoProducto());
         registro.setDescripcionProducto(txtDescPro.getText());
         registro.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
         registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
         registro.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
         registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
         try {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall
        ("{CALL sp_agregarProducto(?, ?, ?, ?, ?, ?, ?, ?)}");
        procedimiento.setString(1, registro.getCodigoProducto());
        procedimiento.setString(2, registro.getDescripcionProducto());
        procedimiento.setDouble(3, registro.getPrecioUnitario());
        procedimiento.setDouble(4, registro.getPrecioDocena());
        procedimiento.setDouble(5, registro.getPrecioMayor());
        procedimiento.setInt(6, registro.getExistencia());
        procedimiento.setInt(7, registro.getCodigoProveedor());
        procedimiento.setInt(8, registro.getCodigoTipoProducto());
        procedimiento.execute();
        
        listaProductos.add(registro);

         }catch (Exception e){
             e.printStackTrace();
         }
     
     }
    
    public void desactivarControles(){
        txtCodigoProd.setEditable(false);
        txtDescPro.setEditable(false);
        txtPrecioU.setEditable(false);
        txtPrecioD.setEditable(false);
        txtPrecioM.setEditable(false);
        txtExistencia.setEditable(false);
        cmbCodigoTipoP.setDisable(true);
        cmbCodigoTipoP.setDisable(true);
    
    }
      public void activarControles(){
        txtCodigoProd.setEditable(true);
        txtDescPro.setEditable(true);
        txtPrecioU.setEditable(true);
        txtPrecioD.setEditable(true);
        txtPrecioM.setEditable(true);
        txtExistencia.setEditable(true);
        cmbCodigoTipoP.setDisable(false);
        cmbCodigoTipoP.setDisable(false);
    
    }
      public void limpiarControles(){
        txtCodigoProd.clear();
        txtDescPro.clear();
        txtPrecioU.clear();
        txtPrecioD.clear();
        txtPrecioM.clear();
        txtExistencia.clear();
        tblProductos.getSelectionModel().getSelectedItem();
        cmbCodigoTipoP.getSelectionModel().getSelectedItem();
        cmbCodigoTipoP.getSelectionModel().getSelectedItem();
    
    }
          public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
}
