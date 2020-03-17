package org.cyk.utility.playground.client.controller.impl;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;
import org.cyk.utility.playground.client.controller.entities.PersonType;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class PersonTypeEditPage extends AbstractEntityEditPageContainerManagedImpl<PersonType> implements Serializable {
	private static final long serialVersionUID = 1L;
	
}
