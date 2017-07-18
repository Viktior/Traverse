package prospector.traverse.util;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.properties.PropertyHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class PropertyString extends PropertyHelper<String> {
    private final HashSet<String> valuesSet;
    private final ArrayList<String> values = new ArrayList<>();

    public PropertyString(String name, Collection<String> values) {
        super(name, String.class);
        this.valuesSet = new HashSet(values);
        this.values.addAll(values);
    }

    public PropertyString(String name, String... values) {
        super(name, String.class);
        this.valuesSet = new HashSet();
        Collections.addAll(this.valuesSet, values);
    }

    @Override
    public Collection<String> getAllowedValues() {
        return ImmutableSet.copyOf(this.valuesSet);
    }

    public ArrayList<String> getAsList() {
        return values;
    }

    @Override
    public Optional<String> parseValue(String value) {
        return this.valuesSet.contains(value) ? Optional.of(value) : Optional.absent();
    }

    @Override
    public String getName(String value) {
        return value;
    }
}
