import React, { useState, useEffect, useCallback } from 'react';
import clsx from 'clsx';
import validate from 'validate.js';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/styles';
import {
  Button,
  TextField,
  Card,
  CardHeader,
  CardContent,
  Divider,
  Switch,
  Avatar,
  colors
} from '@material-ui/core';
import { DropzoneDialog } from 'material-ui-dropzone'
import axios from 'utils/axios';
import {useDropzone} from 'react-dropzone'

const useStyles = makeStyles(theme => ({
  root: {
    
  },
  formGroup: {
    marginBottom: theme.spacing(3)
  },
  fieldGroup: {
    display: 'flex',
    alignItems: 'center'
  },
  textField: {
    '& + &': {
      marginLeft: theme.spacing(2)
    }
  },
  actions: {
    backgroundColor: colors.grey[100],
    paddingTop: theme.spacing(2),
    display: 'flex',
    justifyContent: 'flex-start'
  },
  avatar: {
    height: 72,
    width: 72,
    marginRight: theme.spacing(1)
  }
}));

const schema = {
  name: {
    presence: { allowEmpty: false, message: '^Nome é obrigatório' }
  }
};

const DocumentForm = props => {
  const { document, onSubmit, className, ...rest } = props;

  const [formState, setFormState] = useState({
    isValid: false,
    values: { ...document },
    touched: {},
    errors: {}
  });

  const classes = useStyles();

  useEffect(() => {
    const errors = validate(formState.values, schema);

    setFormState(formState => ({
      ...formState,
      isValid: errors ? false : true,
      errors: errors || {}
    }));
  }, [formState.values]);

  const handleChange = event => {
    event.persist();

    setFormState(formState => ({
      ...formState,
      values: {
        ...formState.values,
        [event.target.name]:
          event.target.type === 'checkbox'
            ? event.target.checked
            : event.target.value
      },
      touched: {
        ...formState.touched,
        [event.target.name]: true
      }
    }));
  };

  const handleSubmit = async event => {
    event.preventDefault();
    onSubmit(formState.values);
  };

  const onDrop = useCallback(files => {
    const data = new FormData();
    data.append('file', files[0]);
    axios({
      method: 'POST',
      url: '/api/v1/document',
      data: data
    })
    .then(response => {
      setFormState(formState => ({
        ...formState,
        values: {
          ...formState.values,
          thumbnail: response.data
        },
        touched: {
          ...formState.touched,
          thumbnail: true
        }
      }));
    }).catch((error) => {
    });
  }, [])
  const {getRootProps, getInputProps} = useDropzone({onDrop})

  const hasError = field =>
    formState.touched[field] && formState.errors[field] ? true : false;

  return (
    <form
        {...rest}
        className={clsx(classes.root, className)}
        onSubmit={handleSubmit}
      >
      <Card
        {...rest}
        className={clsx(classes.root, className)}>
        <CardHeader title="Dados do Documento" action={<Switch
            checked={formState.values.active}
            onChange={handleChange}
            name="active"
            inputProps={{ 'aria-label': 'secondary checkbox' }}
          />} />
        <CardContent>
          <div className={classes.formGroup}>
            <div {...getRootProps({ refKey: 'innerRef' })} className={classes.avatar}>
              <Avatar 
                className={classes.avatar}
                src={formState.values.thumbnail ? formState.values.thumbnail.uri : ''}
              />
            </div>
           <input {...getInputProps()} />
          </div>
          <div className={classes.formGroup}>
            <TextField
              size="small"
              autoFocus
              fullWidth
              className={classes.textField}
              error={hasError('name')}
              helperText={hasError('name') ? formState.errors.name[0] : null}
              label="Nome do Arquivo"
              onChange={handleChange}
              name="name"
              value={formState.values.name}
              variant="outlined"
            />
          </div>
          <div className={classes.formGroup}>
            <div className={classes.fieldGroup}>
              <TextField
                size="small"
                className={classes.textField}
                error={hasError('type')}
                helperText={hasError('type') ? formState.errors.type[0] : null}
                label="Tipo do Arquivo"
                onChange={handleChange}
                name="type"
                value={formState.values.documentType}
                variant="outlined"
              />
              <TextField
                size="small"
                className={classes.textField}
                error={hasError('lastModified')}
                helperText={hasError('lastModified') ? formState.errors.lastModified[0] : null}
                label="Ultima Modificação"
                onChange={handleChange}
                name="lastModified"
                value={formState.values.lastModified}
                variant="outlined"
              />
              </div>
          </div>
          <Divider />
          <div className={classes.formGroup}>
            <div className={classes.fieldGroup}>
              <TextField
                size="small"
                className={classes.textField}
                fullWidth
                error={hasError('author')}
                helperText={hasError('author') ? formState.errors.author[0] : null}
                label="author"
                name="author"
                onChange={handleChange}
                value={formState.values.author}
                variant="outlined"
              />
            </div>
          </div>
        </CardContent>
      </Card>
      <div className={classes.actions}>
        <Button
          size="small"
          color="secondary"
          variant="contained"
          disabled={!formState.isValid}
          type="submit">
          Salvar alterações
        </Button>
      </div>
    </form>
  );
};

DocumentForm.propTypes = {
  document: PropTypes.object.isRequired,
  className: PropTypes.string
};

export default DocumentForm;