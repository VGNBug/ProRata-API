package com.pawsey.prorata.api.component;

import com.pawsey.api.component.BaseComponent;
import com.pawsey.prorata.model.EmploymentEntity;

public class EmploymentComponent extends BaseComponent {

    public EmploymentEntity nameEmploymentEntity(EmploymentEntity employment) {
        if(employment != null) {
            if(employment.getEmployer() != null && (employment.getName() == null || "".equals(employment.getName()))) {
                if(employment.getEmployer().getName() != null) {
                    employment.setName("Employment with " + employment.getEmployer().getName());
                    return employment;
                } else throw new IllegalArgumentException("Supplied EmploymentEntity does contain an EmployerEntity, but it's name is null");
            } else throw new IllegalArgumentException("Supplied EmploymentEntity must be supplied with an EmployerEntity");
        } else throw new IllegalArgumentException("Supplied EmploymentEntity cannot be null");
    }
}
