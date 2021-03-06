package com.crowdin.cli.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.crowdin.cli.BaseCli.RESOURCE_BUNDLE;
import static com.crowdin.cli.properties.PropertiesBuilder.TARGETS;

@EqualsAndHashCode(callSuper = true)
@Data
public class PropertiesWithTargets extends IdProperties {

    static PropertiesWithTargetsConfigurator CONFIGURATOR = new PropertiesWithTargetsConfigurator();

    private List<TargetBean> targets;

    static class PropertiesWithTargetsConfigurator implements PropertiesConfigurator<PropertiesWithTargets> {

        private PropertiesWithTargetsConfigurator() {

        }

        @Override
        public void populateWithValues(PropertiesWithTargets props, Map<String, Object> map) {
            props.setTargets(PropertiesBuilder.getListOfMaps(map, TARGETS)
                .stream()
                .map(TargetBean.CONFIGURATOR::buildFromMap)
                .collect(Collectors.toList()));
        }

        @Override
        public void populateWithDefaultValues(PropertiesWithTargets props) {
            for (TargetBean tb : props.getTargets()) {
                TargetBean.CONFIGURATOR.populateWithDefaultValues(tb);
            }
        }

        @Override
        public PropertiesBuilder.Messages checkProperties(PropertiesWithTargets props, CheckType checkType) {
            PropertiesBuilder.Messages messages = new PropertiesBuilder.Messages();
            if (props.getTargets() == null) {
                if (checkType == CheckType.STANDARD) {
                    messages.addError(RESOURCE_BUNDLE.getString("error.config.missed_section_targets"));
                } else if (checkType == CheckType.LINT) {
                    messages.addWarning(RESOURCE_BUNDLE.getString("warning.config.missed_section_targets"));
                }
            } else if (props.getTargets().isEmpty()) {
                if (checkType == CheckType.STANDARD) {
                    messages.addError(RESOURCE_BUNDLE.getString("error.config.empty_section_target"));
                } else if (checkType == CheckType.LINT) {
                    messages.addWarning(RESOURCE_BUNDLE.getString("warning.config.empty_section_target"));
                }
            } else {
                for (TargetBean tb : props.getTargets())  {
                    messages.addAllErrors(TargetBean.CONFIGURATOR.checkProperties(tb));
                }
            }
            return messages;
        }
    }
}
