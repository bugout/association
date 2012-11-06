package data.tranformer;

import data.Field;

public class FieldTransformer {

	protected FieldMapper mapper;

	public FieldTransformer(FieldMapper mapper) {
		this.mapper = mapper;
	}
	
	public void tranform(Field field) {
		field.setValue(mapper.map(field.getRaw()));
	}
}
